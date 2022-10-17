package Utils

import model.{Author, Document}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val authorFormat: RootJsonFormat[Author] = jsonFormat3(Author)
  implicit val documentFormat: RootJsonFormat[Document] = jsonFormat13(Document)
}
