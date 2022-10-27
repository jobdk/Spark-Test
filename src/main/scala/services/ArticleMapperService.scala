package services

import Utils.DocumentJsonProtocol.documentFormat
import Utils.TimeUtils._
import model.Article
import org.slf4j.{Logger, LoggerFactory}
import services.RedisService.insertIntoRedis
import spray.json._

import scala.util.Random

object ArticleMapperService {
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToArticle(line: String): Unit = {

    // time
    val mapperStartTime = getCurrentTime

    try {
      val article: Article =
        line.mkString.stripMargin.parseJson.convertTo[Article]

      // time
      logTime(
        calculateTimeDifference(mapperStartTime, getCurrentTime),
        MAPPER_FILE_PATH
      )

      // time
      val databaseStartTime = getCurrentTime
      try {
        insertIntoRedis(article)
        println("what up dog" + Random.nextInt())
      } catch {
        case e: Exception ⇒
          LOG.info(e.getMessage)
      }

      // time
      logTime(
        calculateTimeDifference(databaseStartTime, getCurrentTime),
        DATABASE_FILE_PATH
      )
    } catch {
      case e: Exception =>
        LOG.error(e.getMessage)
        System.exit(1)
    }
  }
}
