package services


import services.DatabaseService.initialiseDatabase

import java.io.BufferedReader
import java.sql.Connection
import java.time.Duration
import scala.io.Source

object Test {
  private val fileName = "small.json"

  def main(args: Array[String]): Unit = {
    val connection: Connection = initialiseDatabase()


    val bufferedReader: BufferedReader = Source.fromResource(fileName).bufferedReader()
    val startTime = System.nanoTime
    val mappingAndInsertionTime: (Duration, Duration) = process(bufferedReader.readLine(), bufferedReader, connection, 0)

    val endTime: Long = System.nanoTime()
    val timeSpentReading: Duration = Duration.ofNanos(endTime - startTime).minus(mappingAndInsertionTime._1)
    println("Reading: " + timeSpentReading)
    println("Mapping: " + mappingAndInsertionTime._1)
  }


  def process(line: String, bufferedReader: BufferedReader, connection: Connection, mappingDuration: Long): (Duration, Duration) = {
    if (line == null) {
      return (Duration.ofNanos(mappingDuration), Duration.ofNanos(12321423))

    }

    println(line)
    process(bufferedReader.readLine(), bufferedReader, connection, mappingDuration + DocumentMapperService.mapJsonToListOfDocuments(line, connection))
  }
}
