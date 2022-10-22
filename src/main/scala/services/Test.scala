package services


import java.io.BufferedReader
import java.time.Duration
import scala.io.Source

object Test {
  private val fileName = "small.json"

  def main(args: Array[String]): Unit = {
    val bufferedReader: BufferedReader = Source.fromResource(fileName).bufferedReader()
    val startTime = System.nanoTime
    process(bufferedReader.readLine(), bufferedReader)

    val endTime: Long = System.nanoTime()
    println(Duration.ofNanos(endTime - startTime))
  }


  def process(line: String, bufferedReader: BufferedReader): Unit = {
    if (line == null) return
    println(line)
    process(bufferedReader.readLine(), bufferedReader)
  }
}
