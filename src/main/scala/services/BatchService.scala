package services

import Utils.TimeUtils._
import Utils.Utils.checkLineForPossibleParsingErrors
import org.slf4j.{Logger, LoggerFactory}
import services.DatabaseService.initialiseDatabase

import java.io.BufferedReader
import java.sql.Connection
import scala.annotation.tailrec
import scala.io.Source
import scala.util.Random

object BatchService {
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)
  private val random: Random = Random
  private val BATCH_SIZE = 2000

  def startReading(fileName: String): Unit = {

    val connection: Connection = initialiseDatabase()

    val bufferedReader: BufferedReader =
      Source.fromResource(fileName).bufferedReader()

    val readingStartTime = getCurrentTime
    try {
      bufferedReader
        .lines()
        .forEach(line ⇒ {
          processLines(
            List() :+ checkLineForPossibleParsingErrors(line),
            bufferedReader,
            connection,
            0
          )
        })
    } catch {
      case e: Exception ⇒
        LOG.info(e.getMessage)
    }
    logTime(
      calculateTimeDifference(readingStartTime, getCurrentTime),
      READER_FILE_PATH
    )

    //    calculateDurationsAndPrintTimeResults()

  }

  @tailrec
  def processLines(
      list: List[String],
      bufferedReader: BufferedReader,
      connection: Connection,
      counter: Int
  ): Unit = {
    if (list.size.equals(BATCH_SIZE) | list.last == null) {
      DocumentMapperService.mapJsonToListOfDocuments(
        list.filter(line => line != null),
        connection
      )
      println(list.count(line => line != null) + " " + random.nextInt()) //
      return
    }

    val filteredList: List[String] =
      list.filter(line => lineIsNotNullAndJson(line))
    val validatedLine = checkLineForPossibleParsingErrors(
      bufferedReader.readLine()
    )

    processLines(
      filteredList :+ validatedLine,
      bufferedReader,
      connection,
      counter + 1
    )
  }

  def lineIsNotNullAndJson(line: String): Boolean = {
    line != null && line.endsWith("}")
  }
}
