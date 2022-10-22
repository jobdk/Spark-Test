package Utils

import java.time.Duration

object TimeUtils {
  def calculateTimeDifference(startTime: Long, endTime: Long): Long = {
    endTime - startTime
  }

  def getCurrentTime: Long = System.nanoTime

  def getDuration(time: Long): Duration = {
    Duration.ofNanos(time)
  }

  def calculateTimes(readingStartTime: Long, readingEndTime: Long, mapperAndDatabaseTime: (Long, Long)): Unit = {


    val mapperDuration = getDuration(mapperAndDatabaseTime._1)
    val databaseDuration = getDuration(mapperAndDatabaseTime._2)
    val duration = getDuration(readingEndTime - readingStartTime)
    val readingDuration: Duration = duration.minus(mapperDuration).minus(databaseDuration)

    println("Reading:  " + readingDuration)
    println("Mapper:   " + mapperDuration)
    println("Database: " + databaseDuration)
    println("All time calculated: " + readingDuration.plus(mapperDuration).plus(databaseDuration))
    println("All time actual: " + duration)
  }

}
