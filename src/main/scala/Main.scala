import model.{Article, Author}
import org.apache.spark.sql._
import services.{QueryService, ReaderService}
import utils.TimeUtils
import utils.TimeUtils.{TIME_PATH, getCurrentTime, log}
object Main {
//  private final val fileName =
//    "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-3/src/main/resources/small.json"
  private final val fileName =
    "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-3/src/main/resources/dblp.v12.json"
//  private final val fileName =
//    "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-3/src/main/resources/correct_json.json"

  private val parquetPath = "./parquet/articles_parquet"
  private val PARQUET_VIEW = "parquet"
  private val JSON_VIEW = "json"

  val sparkSession: SparkSession = createSparkSession()

  def main(args: Array[String]): Unit = {

    val startTime = getCurrentTime

/**
 * Reading with multiline and a different separator
 * */
    val jsonDF: Dataset[Article] = ReaderService.readJsonWithLineSeparator(sparkSession, fileName)

    /**reading as text file and line by line which is filted w*/
    val textDF: Dataset[Article] = ReaderService.readLineByLineAsText(sparkSession, fileName)


//    ReaderService.convertToParquet(jsonDF, parquetPath)

//     ReaderService.convertToParquet(textDF, parquetPath)

    val parquetDf: Dataset[Article] =
      ReaderService.readParquet(sparkSession, parquetPath)

    TimeUtils.calculateLogTimeDifference(startTime, "Read Write Read Time         : ")

    jsonDF.createOrReplaceTempView("json")
    parquetDf.createOrReplaceTempView("parquet")

    val countArticlesSqlParquet: Long = QueryService.countArticlesSql(sparkSession, PARQUET_VIEW)
    val countArticlesSparkParquet: Long = QueryService.countArticlesSpark(parquetDf)

    val distinctAuthorsSqlParquet: Long = QueryService.distinctAuthorsSql(sparkSession, PARQUET_VIEW)
    val distinctAuthorsSparkParquet: Long = QueryService.distinctAuthorsSpark(parquetDf)


    val mostArticlesSqlParquet: List[Author] = QueryService.mostArticlesSql(sparkSession, PARQUET_VIEW)
    val mostArticlesSparkParquet: List[Author] = QueryService.mostArticlesSpark(parquetDf)

    TimeUtils.log("", TIME_PATH)

    val countArticlesSqlJson: Long = QueryService.countArticlesSql(sparkSession, JSON_VIEW)
    val countArticlesSparkJson: Long = QueryService.countArticlesSpark(jsonDF)

    val distinctAuthorsSqlJson: Long = QueryService.distinctAuthorsSql(sparkSession, JSON_VIEW)
    val distinctAuthorsSparkJson: Long = QueryService.distinctAuthorsSpark(jsonDF)

    val mostArticlesSqlJson: List[Author] = QueryService.mostArticlesSql(sparkSession, JSON_VIEW)
    val mostArticlesSparkJson: List[Author] = QueryService.mostArticlesSpark(jsonDF)

    // Console Output
    println("Parquet")
    println("countArticlesSqlParquet: " + countArticlesSqlParquet)
    println("countArticlesSparkParquet: " + countArticlesSparkParquet)
    println("distinctAuthorsSqlParquet: " + distinctAuthorsSqlParquet)
    println("distinctAuthorsSparkParquet: " + distinctAuthorsSparkParquet)
    println("mostArticlesSqlParquet: " + mostArticlesSqlParquet)
    println("mostArticlesSparkParquet: " + mostArticlesSparkParquet)

    println("json")
    println("countArticlesSqlJson: " + countArticlesSqlJson)
    println("countArticlesSparkJson: " + countArticlesSparkJson)
    println("distinctAuthorsSqlJson: " + distinctAuthorsSqlJson)
    println("distinctAuthorsSparkJson: " + distinctAuthorsSparkJson)
    println("mostArticlesSqlJson: " + mostArticlesSqlJson)
    println("mostArticlesSparkJson: " + mostArticlesSparkJson)
    log("", TIME_PATH)
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
