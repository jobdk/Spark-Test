package services

import Utils.DocumentJsonProtocol.authorFormat
import model.{Article, Author}
import redis.clients.jedis.{Jedis, Pipeline, Response}
import spray.json._

import java.util
import scala.jdk.CollectionConverters

object RedisService {

  val HOST: String = "localhost"
  val PORT: Int = 6379
  final val jedis: Jedis = startRedis()
  final val ID: String = "id"
  final val TITLE: String = "title"
  final val NAME: String = "name"
  final val ORG = "org"
  final val AUTHOR_SORTED_SET_KEY = "Authors"
  final val AUTHORS_PREFIX = "Authors:"
  final val ARTICLE_PREFIX = "Article:"

  def startRedis(): Jedis = {
    val jedis = new Jedis(HOST, PORT)
    jedis.connect()
    jedis.configSet("appendonly", "no")
    jedis.configSet("save", "")
    jedis
  }

  def test(): Unit = {
    val pipeline = jedis.pipelined()
    val value: Response[util.Set[String]] = pipeline.keys("Article*")
    pipeline.sync()
    pipeline.hget("key", "id")
  }

  def insertIntoRedis(article: Article): Unit = {

    try {

      val pipeline = jedis.pipelined()
      insertArticles(article, pipeline)
      insertAuthor(article, pipeline)
      insertIntoSortedSet(article, pipeline)

      pipeline.sync()
    } catch {
      case e: Exception ⇒
        println(e.getMessage)
    }
  }

  private def insertIntoSortedSet(
      article: Article,
      pipeline: Pipeline
  ): Unit = {
    if (article.authors.get.isEmpty) return
    article.authors.get.foreach(author ⇒ {
      pipeline.zincrby(AUTHOR_SORTED_SET_KEY, 1, author.id.toString)
    })
  }

  private def insertAuthor(
      article: Article,
      pipeline: Pipeline
  ): Unit = {
    if (article.authors.get.isEmpty) return
    article.authors.get.foreach(author ⇒ {
      val key = AUTHORS_PREFIX.concat(author.id.toString)
      pipeline.hset(key, ID, author.id.toString)
      pipeline.hset(key, NAME, author.name)
      pipeline.hset(key, ORG, author.org.getOrElse(""))

    })

  }

  private def insertArticles(
      article: Article,
      pipeline: Pipeline
  ): Unit = {
    val key = ARTICLE_PREFIX.concat(article.id.toString)
    pipeline.hset(key, ID, article.id.toString)
    pipeline.hset(key, TITLE, article.title)
    pipeline.hset(key, "issue", article.issue)
    pipeline.hset(key, "volume", article.volume)
    pipeline.hset(key, "doc_type", article.doc_type)
    pipeline.hset(key, "page_start", article.page_start)
    pipeline.hset(key, "page_end", article.page_end)
    pipeline.hset(key, "publisher", article.publisher)
  }

  // Queries

  def getTitleById(articleId: Long): String = {
    jedis.hget(ARTICLE_PREFIX.concat(articleId.toString), TITLE)
  }

  def getDistinctAuthors(): Long = {
    jedis.zcard(AUTHOR_SORTED_SET_KEY)
  }

  def getMostArticles(): List[Author] = {
    val mostArticles =
      jedis.zrevrangeWithScores(AUTHOR_SORTED_SET_KEY, 0, 0).get(0).getScore
    val listOfAuthorsWithMostArticles: List[String] = CollectionConverters
      .ListHasAsScala(
        jedis.zrangeByScore(
          AUTHOR_SORTED_SET_KEY,
          mostArticles.toLong,
          mostArticles.toLong
        )
      )
      .asScala
      .toList

    getAuthorsById(listOfAuthorsWithMostArticles, jedis)
    null
  }

  def getAuthorsById(
      listOfAuthorsWithMostArticles: List[String],
      jedis: Jedis
  ): List[Author] = {
    val authors: List[util.Map[String, String]] =
      listOfAuthorsWithMostArticles.map(authorId ⇒ {
        jedis.hgetAll(AUTHORS_PREFIX.concat(authorId))
      })
    listOfAuthorsWithMostArticles
      .map(author ⇒ author.parseJson.convertTo[Author])
      .toList
  }
}
