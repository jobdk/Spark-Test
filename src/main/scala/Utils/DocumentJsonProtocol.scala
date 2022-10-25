package Utils

import model.{Author, Article}
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

object DocumentJsonProtocol extends DefaultJsonProtocol {
  implicit val authorFormat: RootJsonFormat[Author] = jsonFormat3(Author)
  implicit val documentFormat: RootJsonFormat[Article] = jsonFormat13(Article)
}
