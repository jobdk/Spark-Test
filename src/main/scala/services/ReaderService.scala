package services

import model.Article
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import spray.json._
import utils.ArticleJsonProtocol.articleFormat
import utils.TimeUtils.getCurrentTime
import utils.{ReaderUtils, TimeUtils}

object ReaderService {

  def readLines(
      sparkSession: SparkSession,
      fileName: String
  ): Dataset[Row] = {
    sparkSession.read
      .textFile(fileName)
      .filter(line => line.endsWith("}"))
      .map(line ⇒ {
        ReaderUtils
          .checkLineForPossibleParsingErrors(line)
          .parseJson
          .convertTo[Article]
      })
  }

  def readWithSeperator(session: SparkSession, fileName: String): Dataset[Row] =
    session
      .read
      .schema(createJsonSchema)
      .option("multiline", false)
      .option("ignoreNullFields", true)
      .option("lineSep", "\n,")
      .json(fileName)

  def readParquet(session: SparkSession, path: String): Dataset[Article] =
    session.read
      .parquet(path)
      .as(articleEncoder)

  def convertToParquet(
      articlesDf: Dataset[Article],
      parquetPath: String
  ): Unit = {
    val startTime: Long = getCurrentTime

    articlesDf.write.parquet(parquetPath)

    TimeUtils.calculateLogTimeDifference(
      startTime,
      "Conversion Read Time: "
    )
  }

  def createJsonSchema: StructType = {
    val authorSchema = StructType(
      Array(
        StructField("id", LongType, nullable = false),
        StructField("name", StringType, nullable = false),
        StructField("org", StringType, nullable = true)
      )
    )

    StructType(
      Array(
        StructField("id", LongType, nullable = false),
        StructField("title", StringType, nullable = false),
        StructField(
          "authors",
          ArrayType(authorSchema, containsNull = false),
          nullable = true
        ),
        StructField("doc_type", StringType, nullable = false),
        StructField("doi", StringType, nullable = true),
        StructField("issue", StringType, nullable = true),
        StructField("n_citation", StringType, nullable = true),
        StructField("page_end", StringType, nullable = true),
        StructField("page_start", StringType, nullable = true),
        StructField("volume", StringType, nullable = true),
        StructField("references", ArrayType(LongType), nullable = true),
        StructField("publisher", StringType, nullable = true),
        StructField("year", StringType, nullable = true)
      )
    )
  }
}
