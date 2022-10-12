package services

import model.{Author, Document}
import spray.json._

import scala.io.{BufferedSource, Source}

object DocumentMapper {

  def mapJsonToListOfDocuments(fileName: String): List[Document] = {

    import MyJsonProtocol._
//    val sour: Iterator[String] = Source.fromResource(fileName).getLines()
    val source: BufferedSource = Source.fromResource(fileName)

    val json: JsValue = source.mkString.stripMargin.parseJson
    json.convertTo[List[Document]]
  }
}

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val authorFormat: RootJsonFormat[Author] = jsonFormat3(Author)
  implicit val documentFormat: RootJsonFormat[Document] = jsonFormat13(Document)

  def read(value: JsValue) = value match {
    case JsArray(Vector(JsString(name), JsNumber(red), JsNumber(green), JsNumber(blue))) =>
      // TODO: add handling for characters which cannot be serialized by spray json -> should be done pre parsing
      println(2)
    case _ => deserializationError("Color expected")
  }
}
