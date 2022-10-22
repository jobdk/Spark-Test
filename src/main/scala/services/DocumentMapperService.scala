package services

import Utils.DocumentJsonProtocol.documentFormat
import model.Document
import org.slf4j.{Logger, LoggerFactory}
import services.DatabaseService.insertDocumentInDatabase
import spray.json._

import java.sql.Connection
import scala.collection.mutable.ListBuffer

object DocumentMapperService {
  val timeList: ListBuffer[Long] = ListBuffer() // time
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToListOfDocuments(line: String, connection: Connection): Unit = {
    val startTimeMapper = System.nanoTime // time

    if (!line.endsWith("}")) return
    val lineToMap = checkLineForPossibleParsingErrors(line)
    try {
      val json: JsValue = lineToMap.mkString.stripMargin.parseJson
      val document: Document = json.convertTo[Document]

      val endTimeReaderMapper = System.nanoTime // time
      timeList.addOne(endTimeReaderMapper - startTimeMapper) // time

      insertDocumentInDatabase(document, connection)
    } catch {
      case e: Exception =>
        LOG.error(e.getMessage)
        System.exit(1)
    }
  }

  def checkLineForPossibleParsingErrors(line: String): String = {
    val lineSubstring: String = if (line.startsWith(",")) line.substring(1, line.length) else line
    if (line.contains("\uFFFF")) lineSubstring.replace("\uFFFF", "x") else lineSubstring
  }
}


