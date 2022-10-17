package services

import Utils.MyJsonProtocol.documentFormat
import model.Document
import org.slf4j.LoggerFactory
import services.DatabaseService.insertDocumentInDatabase
import spray.json._

import java.io.BufferedReader
import java.sql.Connection
import scala.io.Source

object DocumentMapperService {
  val LOG = LoggerFactory.getLogger(getClass.getSimpleName)

  def mapJsonToListOfDocuments(fileName: String, connection: Connection): List[Document] = {
    val bufferedReader: BufferedReader = Source.fromResource(fileName).bufferedReader()
    var counter: Int = 0

    bufferedReader.lines().forEach(line => {
      val lineToMap = checkLineForPossibleParsingErrors(line)
      try {
        val json: JsValue = lineToMap.mkString.stripMargin.parseJson
        insertDocumentInDatabase(json.convertTo[Document], connection)
        counter += 1
      } catch {
        case e: Exception => {
          LOG.error(e.getMessage)
          System.exit(1)
        }
      }
      println(counter)

    })
    null
  }

  def checkLineForPossibleParsingErrors(line: String): String = {
    val lineSubstring: String = if (line.startsWith(",")) line.substring(1, line.length) else line
    if (line.contains("\uFFFF")) line.replace("\uFFFF", "x") else lineSubstring
  }
}


