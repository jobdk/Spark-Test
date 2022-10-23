package Utils

import java.io.FileWriter
import java.time.Duration
import scala.io.Source

object TimeUtils {
  val READER_FILE_PATH = "./src/main/resources/Reading.txt"
  val MAPPER_FILE_PATH = "./src/main/resources/Mapper.txt"
  val DATABASE_FILE_PATH = "./src/main/resources/Database.txt"
  val READER_FILE_NAME = "Reading.txt"
  val MAPPER_FILE_NAME = "Mapper.txt"
  val DATABASE_FILE_NAME = "Database.txt"

  def calculateTimeDifference(startTime: Long, endTime: Long): Long =
    endTime - startTime

  def getCurrentTime: Long = System.nanoTime()

  def getDuration(time: Long): Duration = Duration.ofNanos(time)

  def logTime(time: Long, logFilePath: String): Unit = {
    val readerWriter = new FileWriter(logFilePath, true)
    try {
      readerWriter write time.toString + "\n"
    } finally readerWriter.close()

  }

  def calculateDurationsAndPrintTimeResults(): Unit = {
    Thread.sleep(10000)

    val readingTimeList: List[Long] = Source
      .fromResource(READER_FILE_NAME)
      .getLines()
      .map(line => line.toLong)
      .toList
    val readingDuration = getDuration(readingTimeList.sum)
    val mapperTimeList: List[Long] = Source
      .fromResource(MAPPER_FILE_NAME)
      .getLines()
      .map(line => line.toLong)
      .toList
    val mapperDuration = getDuration(mapperTimeList.sum)
    val databaseTimeList: List[Long] = Source
      .fromResource(DATABASE_FILE_NAME)
      .getLines()
      .map(line => line.toLong)
      .toList
    val databaseDuration = getDuration(databaseTimeList.sum)

    println(
      "Reading:  " + readingDuration
        .minus(mapperDuration)
        .minus(databaseDuration)
    )
    println("Mapper:   " + mapperDuration)
    println("Database: " + databaseDuration)
    println("Full duration: " + readingDuration)

  }

}
