package utils

import utils.ArticleJsonProtocol.{authorFormat, immSetFormat}
import model.{Article, Author}
import spray.json.{
  DefaultJsonProtocol,
  JsValue,
  RootJsonFormat,
  serializationError
}

object ArticleJsonProtocol extends DefaultJsonProtocol {
  implicit val authorFormat: RootJsonFormat[Author] = jsonFormat3(Author)
  implicit val articleFormat: RootJsonFormat[Article] = jsonFormat13(Article)
}

object AuthorSetJsonFormat extends RootJsonFormat[Set[Author]] {
  override def read(json: JsValue): Set[Author] = {
    val jsonFields = json.asJsObject.fields
    jsonFields
      .get("authors")
      .map(a ⇒ {
        if (a.toString() == "null") Set[Author]() else a.convertTo[Set[Author]]
      })
      .getOrElse(Set())
  }

  override def write(obj: Set[Author]): JsValue =
    serializationError(msg = "not supported")
}
