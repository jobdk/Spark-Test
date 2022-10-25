package testing


import Utils.DocumentJsonProtocol.documentFormat
import Utils.TimeUtils.{DATABASE_FILE_PATH, MAPPER_FILE_PATH, calculateTimeDifference, getCurrentTime, logTime}
import model.Article
import org.slf4j.{Logger, LoggerFactory}
import spray.json._
import testing.DatabaseService.insertDocumentInDatabase

import java.sql.Connection
import scala.collection.mutable.ListBuffer

object DocumentMapperService {
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToListOfDocuments(list: List[String]): Unit = {

    // time
    val mapperStartTime = getCurrentTime

    try {
      val documentList: List[Article] =
        list.map(line => line.mkString.stripMargin.parseJson.convertTo[Article])

      // time
      logTime(
        calculateTimeDifference(mapperStartTime, getCurrentTime),
        MAPPER_FILE_PATH
      )

      // time
      val databaseStartTime = getCurrentTime

      insertDocumentInDatabase(documentList)

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
