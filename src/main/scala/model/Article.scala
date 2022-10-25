package model

case class Article(
    id: Long,
    authors: Option[Seq[Author]],
    title: String,
    year: Int,
    n_citation: Int,
    page_start: String,
    page_end: String,
    doc_type: String,
    publisher: String,
    volume: String,
    issue: String,
    doi: String,
    references: Option[Seq[Long]]
    // indexed_abstract: IndexedAbstract,
    // fos: Seq[Fos],
    // venue: Venue
)
