package services

import Utils.DocumentJsonProtocol.documentFormat
import Utils.TimeUtils.calculateTimeDifference
import model.Document
import org.slf4j.{Logger, LoggerFactory}
import services.DatabaseService.insertDocumentInDatabase
import spray.json._

import java.sql.Connection
import scala.collection.mutable.ListBuffer

object DocumentMapperService {
  val timeList: ListBuffer[Long] = ListBuffer() // time
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToListOfDocuments(line: String, connection: Connection): Long = {
    val startTimeMapper = System.nanoTime // time

    if (!line.endsWith("}")) return calculateTimeDifference(startTimeMapper, System.nanoTime())

    val lineToMap = checkLineForPossibleParsingErrors(line)
    try {
      val document: Document = lineToMap.mkString.stripMargin.parseJson.convertTo[Document]
      val mappingDuration = calculateTimeDifference(startTimeMapper, System.nanoTime) // time


      insertDocumentInDatabase(document, connection)

      mappingDuration
    } catch {
      case e: Exception =>
        LOG.error(e.getMessage)
        System.exit(1)
        0
    }
  }

  def checkLineForPossibleParsingErrors(line: String): String = {
    val lineSubstring: String = if (line.startsWith(",")) line.substring(1, line.length) else line
    if (line.contains("\uFFFF")) lineSubstring.replace("\uFFFF", "x") else lineSubstring
  }
}


