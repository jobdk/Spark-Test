package services
import model.{Article, Author}

object QueriesImpl extends Queries {
  override def titleByID(articleID: Long): String = {
    RedisService.getTitleById(articleID: Long)
  }

  override def authors(articleID: Long): List[Author] = ???

  override def articles(authorID: Long): List[Article] = ???

  override def referencedBy(articleID: Long): List[Article] = ???

  override def mostArticles(): List[Author] = {
    RedisService.getMostArticles()
  }

  override def distinctAuthors(): Long = {
    RedisService.getDistinctAuthors()
  }
}
