//import org.slf4j.{Logger, LoggerFactory}
//import services.DatabaseService.initialiseDatabase
//import services.{DatabaseService, DocumentMapperService}
//
//import java.io.BufferedReader
//import java.sql.Connection
//import java.time.Duration
//import scala.io.Source
//
//object Main {
//  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)
//  private val fileName = "dblp.v12.json"
//  //  private val fileName = "small.json"
//  //  private val fileName = "test.json"
//
//  def main(args: Array[String]): Unit = {
//    val connection: Connection = initialiseDatabase()
//    var counter: Int = 0
//    val startTimeReader = System.nanoTime
//    val bufferedReader: BufferedReader =
//      Source.fromResource(fileName).bufferedReader()
//
//    bufferedReader
//      .lines()
//      .forEach(line => {
//        DocumentMapperService.mapJsonToListOfDocuments(line, connection)
//        counter += 1
//        println(counter)
//      })
//    val endTimeReader = System.nanoTime
//    LOG.info(counter + " rows were read")
//    LOG.info(
//      "Time reading data:       " + Duration.ofNanos(
//        (endTimeReader - startTimeReader) - DocumentMapperService.timeList.sum - DatabaseService.timeList.sum
//      )
//    )
//    LOG.info(
//      "Time deserializing data: " + Duration.ofNanos(
//        DocumentMapperService.timeList.sum
//      )
//    )
//    LOG.info(
//      "Time to database:        " + Duration.ofNanos(
//        DatabaseService.timeList.sum
//      )
//    )
//  }
//}
