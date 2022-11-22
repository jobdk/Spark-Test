import model.{Article, Author}
import org.apache.spark.sql._
import services.{QueryService, ReaderService}
import utils.TimeUtils
import utils.TimeUtils.getCurrentTime
object Main {
//  private final val fileName =
//    "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-3/src/main/resources/small.json"
  private final val fileName =
    "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-3/src/main/resources/dblp.v12.json"
//  private final val fileName =
//    "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-3/src/main/resources/correct_json.json"

  private val parquetPath = "./parquet/articles_parquet"

  val sparkSession: SparkSession = createSparkSession()

  def main(args: Array[String]): Unit = {

    val startTime = getCurrentTime
    // -------------------JSON ReaderService-----------------------

    val jsonDF: Dataset[Article] = ReaderService.readJson(sparkSession, fileName)
//    jsonDF.show()

//    val textDF: Dataset[Article] =
//      ReaderService.readEachLine(sparkSession, fileName)

    // -------------------Conversion-------------------------------------

    ReaderService.convertToParquet(jsonDF, parquetPath)

//     ReaderService.convertToParquet(textDF, parquetPath)

    // -------------------Parquet Read-------------------------------------
    val parquetDf: DataFrame =
      ReaderService.readParquet(sparkSession, parquetPath)

    TimeUtils.calculatePrintTimeDifference(startTime, "Read Write Read Time: ")

    // jsonDF.createOrReplaceTempView("json")
    parquetDf.createOrReplaceTempView("parquet")

    // -------------------b) Count Articles-------------------------------------

    val numberArticlesSql: Long = QueryService.countArticlesSql(sparkSession)
    val numberArticlesSpark: Long = QueryService.countArticlesSpark(parquetDf)

    // -------------------c) Count Articles-------------------------------------

//    val numberOfAuthorsSql: Long = QueryService.distinctAuthorsSql(parquetDf, sparkSession)
    val numberOfAuthorsSpark: Long = QueryService.distinctAuthorsSpark(parquetDf, sparkSession)

    val authorsWithMostArticlesSql: List[Author] = QueryService.mostArticlesSql(parquetDf)
    val authorsWithMostArticlesSpark: List[Author] = QueryService.mostArticlesSpark(parquetDf)

    println("numberArticlesSql: " + numberArticlesSql)
    println("numberArticlesSpark: " + numberArticlesSpark)
//    println("numberOfAuthorsSql: " + numberOfAuthorsSql)
    println("numberOfAuthorsSpark: " + numberOfAuthorsSpark)
    println("authorsWithMostArticlesSql: " + authorsWithMostArticlesSql)
    println("authorsWithMostArticlesSpark: " + authorsWithMostArticlesSpark)

  }

  private def createSparkSession(): SparkSession = {
    val sparkSession: SparkSession = SparkSession
      .builder()
      .appName("Praktikum_3")
      .master("local[*]")
      .getOrCreate()
    sparkSession.conf.set("spark.sql.caseSensitive", value = true)
    sparkSession.conf.set("executor-memory", "8192m")
//    sparkSession.conf.set("num-executors", 20)
    sparkSession.sparkContext.setLogLevel("ERROR")

    sparkSession
  }
}
