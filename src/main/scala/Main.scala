import redis.clients.jedis.Jedis
import testing.BatchService.startReading

object Main {
//    private final val fileName = "small.json"
  private final val fileName = "dblp.v12.json"
  val HOST: String = "127.0.0.1"
  val PORT: Int = 6379
  def main(args: Array[String]): Unit = {

    val jedis = new Jedis(HOST, PORT)
    jedis.connect()
    startReading(fileName)
  }
}
