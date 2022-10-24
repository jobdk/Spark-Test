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

  def calculateDatabaseTimeIn10PercentSteps(): Unit = {
    val databaseTimeList: List[Long] = Source
      .fromResource(DATABASE_FILE_NAME)
      .getLines()
      .map(line => line.toLong)
      .toList

    val tenPercentNumber: Int = databaseTimeList.size / 10

    val one: (List[Long], List[Long]) =
      databaseTimeList.splitAt(tenPercentNumber)
    val two: (List[Long], List[Long]) = one._2.splitAt(tenPercentNumber)
    val three: (List[Long], List[Long]) = two._2.splitAt(tenPercentNumber)
    val four: (List[Long], List[Long]) = three._2.splitAt(tenPercentNumber)
    val five: (List[Long], List[Long]) = four._2.splitAt(tenPercentNumber)
    val six: (List[Long], List[Long]) = five._2.splitAt(tenPercentNumber)
    val seven: (List[Long], List[Long]) = six._2.splitAt(tenPercentNumber)
    val eight: (List[Long], List[Long]) = seven._2.splitAt(tenPercentNumber)
    val nine: (List[Long], List[Long]) = eight._2.splitAt(tenPercentNumber)
    val ten: List[Long] = nine._2

    println("one : " + getDuration(one._1.sum))
    println("two : " + getDuration(two._1.sum))
    println("three : " + getDuration(three._1.sum))
    println("four : " + getDuration(four._1.sum))
    println("five : " + getDuration(five._1.sum))
    println("six : " + getDuration(six._1.sum))
    println("seven : " + getDuration(seven._1.sum))
    println("eight : " + getDuration(eight._1.sum))
    println("nine : " + getDuration(nine._1.sum))
    println("ten : " + getDuration(ten.sum))
  }
}
