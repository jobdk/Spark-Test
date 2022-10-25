package services

import model.{Article, Author}

trait Queries {
  def titleByID(articleID: Long): String
  def authors(articleID: Long): List[Author]
  def articles(authorID: Long): List[Article]
  def referencedBy(articleID: Long): List[Article]
}
