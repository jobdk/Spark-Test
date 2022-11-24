package utils

import java.io.FileWriter
import java.time.{Duration, LocalDateTime}
import java.util.Calendar

object TimeUtils {
  val TIME_PATH = "./src/main/resources/Times.txt"

  def calculatePrintTimeDifference(startTime: Long, text: String): Unit = {
    val content = text + getDuration(getCurrentTime - startTime)
    val date = Calendar.getInstance().getTime
    logTime(date + ": " + content, TIME_PATH)
    println(content)
  }

  def getCurrentTime: Long = System.nanoTime()

  def getDuration(time: Long): Duration = Duration.ofNanos(time)

  def logTime(content: String, logFilePath: String): Unit = {
    val readerWriter = new FileWriter(logFilePath, true)
    try {
      readerWriter write content + "\n"
    } finally readerWriter.close()

  }
}
