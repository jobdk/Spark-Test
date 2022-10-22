package services


import Utils.TimeUtils.{calculateTimes, getCurrentTime}
import services.DatabaseService.initialiseDatabase

import java.io.BufferedReader
import java.sql.Connection
import scala.annotation.tailrec
import scala.io.Source

object Test {
  //  private val fileName = "small.json"
  private val fileName = "dblp.v12.json"

  def main(args: Array[String]): Unit = {
    val connection: Connection = initialiseDatabase()


    val bufferedReader: BufferedReader = Source.fromResource(fileName).bufferedReader()
    val readingStartTime = getCurrentTime()
    val mapperAndDatabaseTime: (Long, Long) = process(bufferedReader.readLine(), bufferedReader, connection, (0, 0), 0, readingStartTime)

    calculateTimes(readingStartTime, getCurrentTime(), mapperAndDatabaseTime)



    //    val readerEndTime: Long = getCurrentTime()
    //    val timeSpentReading: Duration = Duration.ofNanos(readerEndTime - readingStartTime).minus(mapperAndDatabaseTime._1).minus(mapperAndDatabaseTime._2)
    //    println("Reading: " + timeSpentReading)
    //    println("Mapping: " + mapperAndDatabaseTime._1)
    //    println("Database: " + mapperAndDatabaseTime._2)
    //    println("All time calculated: " + timeSpentReading.plus(mapperAndDatabaseTime._1).plus(mapperAndDatabaseTime._2))
    //    println("All time actual: " + Duration.ofNanos(readerEndTime - readingStartTime))


  }


  @tailrec
  def process(line: String, bufferedReader: BufferedReader, connection: Connection, mapperAndDatabaseTime: (Long, Long), counter: Int, readingStartTime: Long): (Long, Long) = {

    if (line == null) return (mapperAndDatabaseTime._1, mapperAndDatabaseTime._2)


    if (counter == 10000) {
      calculateTimes(readingStartTime, getCurrentTime(), mapperAndDatabaseTime)
    }
    val time: (Long, Long) = DocumentMapperService.mapJsonToListOfDocuments(line, connection)
    println(counter)
    process(
      bufferedReader.readLine(),
      bufferedReader,
      connection,
      (mapperAndDatabaseTime._1 + time._1, mapperAndDatabaseTime._2 + time._2), counter + 1, readingStartTime
    )
  }
}
