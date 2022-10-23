import Utils.TimeUtils._
import org.scalatest.funsuite.AnyFunSuite

import java.io.{File, FileWriter}

class Test extends AnyFunSuite {
  test("should delete and recreate log files") {
    deleteLogs()
    createLogs()
  }

  test("should calculate times") {
    calculateDurationsAndPrintTimeResults()
  }

  def createLogs(): Unit = {
    val readerWriter = new FileWriter(READER_FILE_PATH)
    try {
      readerWriter write ("")
    } finally readerWriter.close()

    val mapperWriter = new FileWriter(MAPPER_FILE_PATH)
    try {
      mapperWriter write ("")
    } finally mapperWriter.close()

    val databaseWriter = new FileWriter(DATABASE_FILE_PATH)
    try {
      databaseWriter write ("")
    } finally databaseWriter.close()
  }

  def deleteLogs(): Unit = {
    new File(READER_FILE_PATH).delete()
    new File(MAPPER_FILE_PATH).delete()
    new File(DATABASE_FILE_PATH).delete()
  }

  test("should calculate times for database") {
    val one = 53745369916L
    val two = 97116991123L
    val three = 136236149135L
    val four = 174039645535L
    val five = 194605455550L
    val six = 228635628204L
    val seven = 218577812341L
    val eight = 248544681171L
    val nine = 248167872007L
    val ten = 255503630453L
    println("1 = " + getDuration(one))
    println("2 = " + getDuration(two))
    println("3 = " + getDuration(three))
    println("4 = " + getDuration(four))
    println("5 = " + getDuration(five))
    println("6 = " + getDuration(six))
    println("7 = " + getDuration(seven))
    println("8 = " + getDuration(eight))
    println("9 = " + getDuration(nine))
    println("10 = " + getDuration(ten))
  }
}
