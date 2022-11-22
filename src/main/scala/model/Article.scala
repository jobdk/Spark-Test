package model

case class Article(
    id: String,
    authors: Option[Seq[Author]],
    title: String,
//    year: Int,
//    n_citation: Int,
    year: String,
    n_citation: String,
    page_start: String,
    page_end: String,
    doc_type: String,
    publisher: String,
    volume: String,
    issue: String,
    doi: String,
    references: Option[Seq[String]]
)
