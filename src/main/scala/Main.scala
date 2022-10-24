import Utils.SqlCommands.{ADD_ALL_KEYS, DROP_ALL_KEYS}
import Utils.TimeUtils.{getCurrentTime, getDuration}
import services.BatchService.startReading
import services.DatabaseService.runSqlCommand

object Main {
//    private final val fileName = "small.json"
  private final val fileName = "dblp.v12.json"

  def main(args: Array[String]): Unit = {

    runSqlCommand(DROP_ALL_KEYS)

    val startTime = getCurrentTime

    startReading(fileName)

    println(getDuration(getCurrentTime - startTime))

    runSqlCommand(ADD_ALL_KEYS)
  }
}
