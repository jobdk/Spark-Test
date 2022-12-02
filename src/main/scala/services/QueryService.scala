package services

import model.{Article, Author}
import org.apache.spark.sql._
import org.apache.spark.sql.functions.{col, explode}
import utils.ArticleJsonProtocol.authorFormat
import utils.TimeUtils
import utils.TimeUtils.getCurrentTime
//import spray.json._
import spray.json._

//import org.apache.spark.sql._
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.types._

object QueryService {

  implicit val articleSetEncoder: Encoder[Set[Article]] = Encoders.bean(classOf[Set[Article]])
  implicit val articleEncoder: Encoder[Article] = Encoders.product[Article]
  implicit val authorEncoder: Encoder[Author] = Encoders.product[Author]
  implicit val stringEncoder: Encoder[String] = Encoders.STRING
  implicit val longEncoder: Encoder[Long] = Encoders.scalaLong

  def countArticlesSql(sparkSession: SparkSession, dataSource: String): Long = {
    val startTime: Long = getCurrentTime

    val parquetSqlCount: Long =
      sparkSession.sql("select count(*) from " + dataSource).collect().head.getAs[Long](0)

    TimeUtils.calculateLogTimeDifference(startTime, "SQL   number of articles time: ")
    parquetSqlCount
  }

  def countArticlesSpark(dataFrame: Dataset[Article] ): Long = {
    val startTime = getCurrentTime

    val parquetSparkCount = dataFrame.select("id").count()

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "Spark number of articles time: "
    )
    parquetSparkCount
  }

  def distinctAuthorsSql(sparkSession: SparkSession, prefix: String): Long = {
    getDistinctAuthorWithArrayDistinct(sparkSession, prefix)
    // explode is faster
    getDistinctAuthorWithExplode(sparkSession, prefix)

  }

  def getDistinctAuthorWithExplode(sparkSession: SparkSession, prefix: String): Long = {
    val startTime = getCurrentTime

    sparkSession
      .sql("select authors from parquet")
      .createOrReplaceTempView(prefix + "Authors")
    sparkSession.sql("select explode(authors) as author from authors").createOrReplaceTempView("allAuthors")
    sparkSession
      .sql("select author.id from allAuthors group by author.id")
      .createOrReplaceTempView("distinctAuthors")
    val distinctAuthors: Long = sparkSession
      .sql("select count(*) from distinctAuthors")
      .first()
      .get(0)
      .toString
      .toLong

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "SQL expl distinctAuthors time: "
    )
    distinctAuthors

  }
  private def getDistinctAuthorWithArrayDistinct(sparkSession: SparkSession, dataSource: String) = {
    val startTime = getCurrentTime

    sparkSession
      .sql("select authors from " + dataSource)
      .createOrReplaceTempView("authors")

    val numberOfDistinctAuthors: Long = sparkSession
      .sql(
        """select
          size(
            array_distinct(
              flatten(
                array_agg(
                  authors.id
                )
              )
            )
          )
          as numberOfDistinctAuthors
          from authors""".stripMargin
      )
      .first()
      .get(0)
      .toString
      .toLong

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "SQL arr distinctAuthors time : "
    )
    numberOfDistinctAuthors
  }

  def distinctAuthorsSpark(dataFrame: Dataset[Article] ): Long = {
    val startTime = getCurrentTime

    val distinctAuthors: Long = dataFrame
      .select(explode(col("authors")))
      .select("col.id", "col.name", "col.org")
      .select("id")
      .distinct()
      .count()

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "Spark distinctAuthors time   : "
    )
    distinctAuthors
  }

  def mostArticlesSql(sparkSession: SparkSession, dataSource:String): List[Author] = {
    val startTime = getCurrentTime

    sparkSession
      .sql("select authors from "+dataSource)
      .createOrReplaceTempView("authors")

    sparkSession
      .sql("select explode(authors) as author from authors")
      .createOrReplaceTempView("allAuthors")

    sparkSession
      .sql("""select
       author.id,
        max(author.name) as name,
        max(author.org) as org,
        count(author.id) as number
        from allAuthors
        group by author.id
        order by number desc
        """.stripMargin)
      .createOrReplaceTempView("distinctAuthors")

    val highScore =
      sparkSession
        .sql("select MAX(distinctAuthors.number) from distinctAuthors")
        .first()
        .get(0)
        .toString
        .toLong

    val authorsWithMostArticles: DataFrame = sparkSession
      .sql("select id, name, org from distinctAuthors where distinctAuthors.number = " + highScore)

    val authors: List[Author] = authorsWithMostArticles
      .map(row ⇒ {
        row.json.parseJson.convertTo[Author]
      })
      .collect()
      .toList

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "SQL   mostArticles           : "
    )
    authors
  }

  def mostArticlesSpark(dataFrame: Dataset[Article] ): List[Author] = {
    val startTime = getCurrentTime

    val duplicateAuthors = dataFrame
      .select(explode(col("authors")))

    val numberOfArticlesByAuthor = duplicateAuthors
      .groupBy("col.id")
      .count()

    val distinctAuthors = duplicateAuthors
      .select(
        duplicateAuthors.col("col.id"),
        duplicateAuthors.col("col.name"),
        duplicateAuthors.col("col.org")
      )
      .dropDuplicates("id")

    val joined = distinctAuthors
      .join(numberOfArticlesByAuthor, "id")

    val highScore: Long = joined
      .select(functions.max("count"))
      .first()
      .get(0)
      .toString
      .toLong

    val authors: List[Author] = joined
      .filter("count = " + highScore)
      .select("id", "name", "org")
      .map(row ⇒ {
        row.json.parseJson.convertTo[Author]
      })
      .collect()
      .toList

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "Spark mostArticles           : "
    )
    authors
  }

}
