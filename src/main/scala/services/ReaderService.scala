package services

import model.Article
import org.apache.spark.sql._
import org.apache.spark.sql.types._
import spray.json._
import utils.ArticleJsonProtocol.articleFormat
import utils.TimeUtils.getCurrentTime
import utils.{ReaderUtils, TimeUtils}

object ReaderService {
  implicit val articleEncoder: Encoder[Article] = Encoders.product[Article]

  def readLineByLineAsText(
      sparkSession: SparkSession,
      fileName: String
  ): Dataset[Article] = {
    sparkSession.read
      .textFile(fileName)
      .filter(line => line.endsWith("}"))
      .map(line ⇒ {
        ReaderUtils
          .checkLineForPossibleParsingErrors(line)
          .parseJson
          .convertTo[Article]
      })
      .as(articleEncoder)
  }

  def readJsonWithLineSeparator(session: SparkSession, fileName: String): Dataset[Article] =
    session
//      .read
//      //        .option("multiline", value = true) // created valid json
//      .option("lineSep", "\r\n,")
//      .option("ignoreNullFields", true)
//      .schema(createJsonSchema)
//      .json(fileName)
//      .as(articleEncoder)
      .read
      .schema(createJsonSchema)
      .option("multiline", value = false)
      .option("ignoreNullFields", value = true)
      .option("lineSep", "\n,")
      .json(fileName)
      .as(articleEncoder)

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
