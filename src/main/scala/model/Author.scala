package model

case class Author(
    id: Long,
    name: String,
    org: Option[String]
)
