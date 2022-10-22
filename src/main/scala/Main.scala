import org.slf4j.{Logger, LoggerFactory}
import services.DatabaseService.initialiseDatabase
import services.{DatabaseService, DocumentMapperService}

import java.io.BufferedReader
import java.sql.Connection
import java.time.Duration
import scala.io.Source


object Main {
  private val LOG: Logger = LoggerFactory.getLogger(getClass.getSimpleName)
  private val fileName = "dblp.v12.json"
  //  private val fileName = "small.json"
  //  private val fileName = "test.json"

  def main(args: Array[String]): Unit = {
    val connection: Connection = initialiseDatabase()
    var counter: Int = 0
    val startTimeReader = System.nanoTime
    val bufferedReader: BufferedReader = Source.fromResource(fileName).bufferedReader()

    bufferedReader.lines().forEach(line => {
      DocumentMapperService.mapJsonToListOfDocuments(line, connection)
      counter += 1
      println(counter)
    })
    val endTimeReader = System.nanoTime
    LOG.info(counter + " rows were read")
    LOG.info("Time reading data:       " + Duration.ofNanos((endTimeReader - startTimeReader) - DocumentMapperService.timeList.sum - DatabaseService.timeList.sum))
    LOG.info("Time deserializing data: " + Duration.ofNanos(DocumentMapperService.timeList.sum))
    LOG.info("Time to database:        " + Duration.ofNanos(DatabaseService.timeList.sum))
  }
}

/*
* Aufgabe 3
* So lange wie die Serealisierung und das einlesen in die Datenbank,da sie Objekt für Objekt eingelesen wird
*
*
*4894083
[main] INFO Main$ - 4894083 Documents were imported.
[main] INFO Main$ - Time reading data:       PT2M7.671678336S
[main] INFO Main$ - Time deserializing data: PT5M2.835406784S
[main] INFO Main$ - Time to database:        PT1H57M25.672997672S
*
*
*
* Autor       4398138
* Artikel     4894081
* Referenzen  45564149
*
* Ohne Primary Key
Time reading data:       PT1M3.752299036S
Time deserializing data: PT4M2.282996789S
Time to database:        PT5M39.007333341S

*
* */