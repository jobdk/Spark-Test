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

  test("should calculate times for database") {
    calculateDatabaseTimeIn10PercentSteps()
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
}
