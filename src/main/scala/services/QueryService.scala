package services

import model.{Article, Author}
import org.apache.spark.sql._
import org.apache.spark.sql.functions.{col, explode}
import utils.TimeUtils
import utils.TimeUtils.getCurrentTime
//import spray.json._

//import org.apache.spark.sql._
//import org.apache.spark.sql.functions._
//import org.apache.spark.sql.types._

object QueryService {

  implicit val articleSetEncoder: Encoder[Set[Article]] = Encoders.bean(classOf[Set[Article]])
  implicit val articleEncoder: Encoder[Article] = Encoders.product[Article]
  implicit val authorEncoder: Encoder[Author] = Encoders.product[Author]
  implicit val stringEncoder: Encoder[String] = Encoders.STRING
  implicit val longEncoder: Encoder[Long] = Encoders.scalaLong

  def countArticlesSql(sparkSession: SparkSession): Long = {
    val startTime: Long = getCurrentTime

    val parquetSqlCount: Long = sparkSession.sql("select count(*) from parquet").collect().head.getAs[Long](0)

    TimeUtils.calculatePrintTimeDifference(startTime, "SQL Count Articles Time: ")
    parquetSqlCount
  }

  def countArticlesSpark(parquetDf: DataFrame): Long = {
    val startTime = getCurrentTime

    val parquetSparkCount = parquetDf.select("id").count()

    TimeUtils.calculatePrintTimeDifference(
      startTime,
      "Spark Count Articles Time: "
    )
    parquetSparkCount
  }

  def distinctAuthorsSql(parquetDf: DataFrame, sparkSession: SparkSession): Long = {
    val startTime = getCurrentTime

    sparkSession
      .sql("select authors from parquet")
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
//      .collect()
//      .head
//      .getAs[Long](0)
      .first()
      .get(0)
      .toString
      .toLong

    TimeUtils.calculatePrintTimeDifference(
      startTime,
      "SQL Number of authors time: "
    )
    numberOfDistinctAuthors
  }

  def distinctAuthorsSpark(parquetDf: DataFrame, sparkSession: SparkSession): Long = { // TODO: Change to different syntax

    parquetDf
      .select(explode(col("authors")))
//      .as("author")
      .select("col.id", "col.name", "col.org")
      .as(authorEncoder)
      .select("id")
      .distinct()
      .count()
    /*  sparkSession
      .read
      .parquet("")
      .select("authors")
      .flatMap(authors => authors.json.parseJson.convertTo[Author])
      .cache()

    val value: Dataset[Row] = parquetDf
      .select(explode(col("authors")))
      .as("author")

      .select("author.id", "author.name", "author.org")
      .as(authorEncoder)*/
  }

  def mostArticlesSql(parquetDf: DataFrame): List[Author] = {

//      parquetDf
//        .select("authors")
//        .flatMap(authors => authors.json.parseJson.convertTo)
//        .createTempView("authors")
//
//      sparkSession
//        .sql(
//          """
//                 SELECT id, COUNT(*) as count
//                 FROM authors
//                 GROUP BY id
//                 """.stripMargin
//        )
//        .createTempView("authorFrequencyMapping")
//
//      sparkSession
//        .sql(
//          """
//                 SELECT id
//                 FROM authorFrequencyMapping
//                 WHERE count = (SELECT MAX(count) FROM authorFrequencyMapping)
//                 """.stripMargin
//        )
//        .createTempView("maxIds")
//
//      sparkSession
//        .sql(
//          """
//                 SELECT DISTINCT a.id, a.name, a.org
//                 FROM authors as a
//                 JOIN maxIds as m
//                 WHERE m.id = a.id
//                 """.stripMargin
//        )
//        .as(authorEncoder)
//        .collect()
//        .groupBy(author => author.id)
//        .map(authors => authors._2.head)
//        .toSet
    ???
  }

  def mostArticlesSpark(parquetDf: DataFrame): List[Author] = {

//      val authorDataset = parquetDf
//        .select("authors")
//        .flatMap(authors => authors.json.parseJson.convertTo)
//
//      val authorIdsWithCount = authorDataset
//        .groupBy("id")
//        .count()
//
//      val maxCount = authorIdsWithCount
//        .agg(max(col("count")))
//        .withColumnRenamed("max(count)", "count")
//
//      val maxAuthorIds = authorIdsWithCount
//        .join(maxCount, usingColumn = "count")
//
//      authorDataset
//        .join(maxAuthorIds, usingColumn = "id")
//        .dropDuplicates("id")
//        .select("id", "name", "org")
//        .as(authorEncoder)
//        .collect()
//        .toSet
    ???
  }

}
