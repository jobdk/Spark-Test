package utils

import java.time.Duration

object TimeUtils {
  def calculatePrintTimeDifference(startTime: Long, text: String): Unit =
    println(text + getDuration(getCurrentTime - startTime))

  def getCurrentTime: Long = System.nanoTime()

  def getDuration(time: Long): Duration = Duration.ofNanos(time)

}
