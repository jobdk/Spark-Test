package services

import Utils.DocumentJsonProtocol.documentFormat
import Utils.TimeUtils.{calculateTimeDifference, getCurrentTime}
import model.Document
import org.slf4j.{Logger, LoggerFactory}
import services.DatabaseService.insertDocumentInDatabase
import spray.json._

import java.sql.Connection
import scala.collection.mutable.ListBuffer

object DocumentMapperService {
  val timeList: ListBuffer[Long] = ListBuffer() // time
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToListOfDocuments(line: String, connection: Connection): (Long, Long) = {
    val mapperStartTime = getCurrentTime() // time

    if (!line.endsWith("}")) return (calculateTimeDifference(mapperStartTime, getCurrentTime()), 0) // time

    val lineToMap = checkLineForPossibleParsingErrors(line)

    try {
      val document: Document = lineToMap.mkString.stripMargin.parseJson.convertTo[Document]

      val mappingTime = calculateTimeDifference(mapperStartTime, getCurrentTime()) // time

      val DatabaseStartTime = getCurrentTime() // time

      insertDocumentInDatabase(document, connection)

      val databaseTime = calculateTimeDifference(DatabaseStartTime, System.nanoTime) // time

      (mappingTime, databaseTime)
    } catch {
      case e: Exception =>
        LOG.error(e.getMessage)
        System.exit(1)
        (0, 0)
    }
  }

  def checkLineForPossibleParsingErrors(line: String): String = {
    val lineSubstring: String = if (line.startsWith(",")) line.substring(1, line.length) else line
    if (line.contains("\uFFFF")) lineSubstring.replace("\uFFFF", "x") else lineSubstring
  }
}


