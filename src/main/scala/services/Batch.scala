package services

import Utils.TimeUtils.{getCurrentTime, getDuration}
import services.BatchService.startReading

object Batch {
  //  private val fileName = "small.json"
  private val fileName = "dblp.v12.json"

  def main(args: Array[String]): Unit = {
    val startTime = getCurrentTime
    startReading(fileName)
    println(getDuration(getCurrentTime - startTime))
  }
}
