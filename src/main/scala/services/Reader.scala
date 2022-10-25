package services

import Utils.TimeUtils.{READER_FILE_PATH, calculateTimeDifference, getCurrentTime, logTime}
import Utils.Utils.checkLineForPossibleParsingErrors
import org.slf4j.{Logger, LoggerFactory}

import java.io.BufferedReader
import scala.annotation.tailrec
import scala.io.Source

object Reader {
  final private val LOG: Logger =
    LoggerFactory.getLogger(getClass.getSimpleName)
  final private val BATCH_SIZE = 2000

  def startReading(fileName: String): Unit = {

    val bufferedReader: BufferedReader =
      Source.fromResource(fileName).bufferedReader()

    val readingStartTime = getCurrentTime

    try {
      bufferedReader
        .lines()
        .forEach(line ⇒ {
          processLines(
            List() :+ checkLineForPossibleParsingErrors(line),
            bufferedReader
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
  }

  @tailrec
  def processLines(
      list: List[String],
      bufferedReader: BufferedReader
  ): Unit = {
    if (batchSizeIsReachedOrLastEntryIsNull(list)) {
      DocumentMapperService.mapJsonToListOfDocuments(
        list.filter(line => line != null)
      )
      println(list.count(line => line != null))
      println("______")
      return
    }

    val filteredList: List[String] =
      list.filter(line => lineIsNotNullAndJson(line))

    processLines(
      filteredList :+ checkLineForPossibleParsingErrors(
        bufferedReader.readLine()
      ),
      bufferedReader
    )
  }

  private def lineIsNotNullAndJson(line: String): Boolean = {
    line != null && line.endsWith("}")
  }

  private def batchSizeIsReachedOrLastEntryIsNull(list: List[String]): Boolean =
    list.size.equals(BATCH_SIZE) | list.last == null
}