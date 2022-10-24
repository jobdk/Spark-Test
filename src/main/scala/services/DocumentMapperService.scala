package services

import Utils.DocumentJsonProtocol.documentFormat
import Utils.TimeUtils._
import model.Document
import org.slf4j.{Logger, LoggerFactory}
import services.DatabaseService.insertDocumentInDatabase
import spray.json._

object DocumentMapperService {
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToListOfDocuments(list: List[String]): Unit = {

    // time
    val mapperStartTime = getCurrentTime

    try {
      val documentList: List[Document] = list.map(line =>
        line.mkString.stripMargin.parseJson.convertTo[Document]
      )

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
