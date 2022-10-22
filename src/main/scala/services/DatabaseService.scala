package services

import model.Document

import java.sql.{Connection, DriverManager}
import scala.collection.mutable.ListBuffer

object DatabaseService {
  val timeList: ListBuffer[Long] = ListBuffer() // time

  def initialiseDatabase(): Connection = {
    Class.forName("org.h2.Driver")
    val connection = DriverManager.getConnection("jdbc:h2:./src/main/resources/DocumentsDatabase;MODE=MYSQL")
    val createSchemaStatements = connection.createStatement()
    val createSchemaStatementsString =
      """
            DROP TABLE if EXISTS authorOfDocument;
            DROP TABLE if EXISTS documentReferences;
            DROP TABLE if EXISTS document;
            DROP TABLE if EXISTS author;

            CREATE TABLE document
            (
                document_id   LONG NOT NULL,
                title         VARCHAR(800),
                document_year Int,
                n_citation    Int,
                page_start    VARCHAR(100),
                page_end      VARCHAR(100),
                doc_type      VARCHAR(800),
                publisher     VARCHAR(800),
                volume        VARCHAR(800),
                issue         VARCHAR(800),
                doi           VARCHAR(800)
            );

            CREATE TABLE author
            (
                author_id LONG NOT NULL,
                name      VARCHAR(300),
                org       VARCHAR(800)
            );

            CREATE TABLE authorOfDocument
            (
                document_id LONG NOT NULL,
                author_id   LONG NOT NULL
            );

            CREATE TABLE documentReferences
            (
                document_id  LONG NOT NULL,
                reference_id LONG NOT NULL
            );
          """.stripMargin
    createSchemaStatements.execute(createSchemaStatementsString)
    connection
  }


  def insertDocumentInDatabase(document: Document, connection: Connection): Unit = {
    val startTimeReader = System.nanoTime // time

    insertIntoDocumentTable(document, connection)
    insertIntoAuthorTable(document, connection)
    insertIntoDocumentReferencesTable(document, connection)

    val endTimeReader = System.nanoTime // time
    timeList.addOne(endTimeReader - startTimeReader) // time
  }

  private def insertIntoDocumentTable(document: Document, connection: Connection): Unit = {
    val insertDocumentStatement = connection.prepareStatement(
      s"INSERT INTO DOCUMENT (DOCUMENT_ID, TITLE, DOCUMENT_YEAR, N_CITATION, PAGE_START, PAGE_END, DOC_TYPE, PUBLISHER, VOLUME, ISSUE, DOI) VALUES (?, ?, ?,?, ?, ?,?, ?, ?,?,?)")
    insertDocumentStatement.setLong(1, document.id)
    insertDocumentStatement.setString(2, document.title)
    insertDocumentStatement.setInt(3, document.year)
    insertDocumentStatement.setInt(4, document.n_citation)
    insertDocumentStatement.setString(5, document.page_start)
    insertDocumentStatement.setString(6, document.page_end)
    insertDocumentStatement.setString(7, document.doc_type)
    insertDocumentStatement.setString(8, document.publisher)
    insertDocumentStatement.setString(9, document.volume)
    insertDocumentStatement.setString(10, document.issue)
    insertDocumentStatement.setString(11, document.doi)
    insertDocumentStatement.execute()
  }

  private def insertIntoAuthorTable(document: Document, connection: Connection): Unit = {
    if (document.authors.isEmpty) return

    document.authors.orNull.foreach(author => {
      val insertAuthorStatement = connection.prepareStatement(
        s"INSERT INTO AUTHOR (AUTHOR_ID, NAME, ORG) VALUES (?, ?, ?)")
      insertAuthorStatement.setLong(1, author.id)
      insertAuthorStatement.setString(2, author.name)
      insertAuthorStatement.setString(3, author.org.orNull)
      insertAuthorStatement.execute()
      insertIntoAuthorOfDocumentTable(document.id, author.id, connection)
    })
  }

  private def insertIntoAuthorOfDocumentTable(documentId: Long, authorId: Long, connection: Connection): Unit = {
    val insertAuthorOfDocumentStatement = connection.prepareStatement(
      s"INSERT INTO AUTHOROFDOCUMENT (DOCUMENT_ID, AUTHOR_ID) VALUES (?, ?)")
    insertAuthorOfDocumentStatement.setLong(1, documentId)
    insertAuthorOfDocumentStatement.setLong(2, authorId)
    insertAuthorOfDocumentStatement.execute()
  }

  private def insertIntoDocumentReferencesTable(document: Document, connection: Connection): Unit = {
    if (document.references.isEmpty) return
    document.references.get.foreach(reference => {
      val insertDocumentReferencesStatement = connection.prepareStatement(
        s"INSERT INTO DOCUMENTREFERENCES (DOCUMENT_ID, REFERENCE_ID) VALUES (?, ?)")
      insertDocumentReferencesStatement.setLong(1, document.id)
      insertDocumentReferencesStatement.setLong(2, reference)
      insertDocumentReferencesStatement.execute()
    })
  }
}
