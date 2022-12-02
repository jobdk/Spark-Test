package utils

import java.io.FileWriter
import java.time.Duration
import java.util.Calendar

object TimeUtils {
  val TIME_PATH = "./src/main/resources/Times.txt"

  def calculateLogTimeDifference(startTime: Long, text: String): Unit = {
    val content = text + getDuration(getCurrentTime - startTime)
    val date = Calendar.getInstance().getTime
    log(date + ": " + content, TIME_PATH)
  }

  def getCurrentTime: Long = System.nanoTime()

  def getDuration(time: Long): Duration = Duration.ofNanos(time)

  def log(content: String, logFilePath: String): Unit = {
    val readerWriter = new FileWriter(logFilePath, true)
    try {
      readerWriter write content + "\n"
    } finally readerWriter.close()

  }
}
