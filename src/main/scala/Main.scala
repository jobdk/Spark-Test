import Utils.TimeUtils.{calculateTimeDifference, getCurrentTime, getDuration}
import model.Author
import services.QueriesImpl.{distinctAuthors, mostArticles}
import services.Reader.startReading

object Main {
//    private final val fileName = "small.json"
  private final val fileName = "dblp.v12.json"

  def main(args: Array[String]): Unit = {
//    val startTime = getCurrentTime
//    startReading(fileName)
//    println(getDuration(calculateTimeDifference(startTime, getCurrentTime)))

    println("Aufgabe 6")
    val authorsWithMostArticles: List[Author] = mostArticles()
    authorsWithMostArticles.foreach(author ⇒ {
      println(s"Author id ${author.id} name: ${author.name} org. ${author.org}")
    })

    println("Aufgabe 7")
    distinctAuthors()

    //   test()

  }
}
