package services

import Utils.SqlCommands._
import model.Document

import java.sql.{Connection, DriverManager}

object DatabaseService {
  val connection: Connection = initialiseDatabase()

  def initialiseDatabase(): Connection = {
    Class.forName(DATABASE_DRIVER)
    val connection = DriverManager.getConnection(DATABASE_CONNECTION_EMBEDDED)
    connection.setAutoCommit(false)
    val createSchemaStatements = connection.createStatement()
    //  createSchemaStatements.execute(CREATE_SCHEMA_STATEMENTS_WITHOUT_PRIMARY_KEY)
    createSchemaStatements.execute(CREATE_SCHEMA_STATEMENTS_WITH_PRIMARY_KEY)
    createSchemaStatements.close()
    connection
  }

  def insertDocumentInDatabase(
      documentList: List[Document]
//      connection: Connection
  ): Unit = {
    insertIntoDocumentTable(documentList, connection)
    insertIntoAuthorTable(documentList, connection)
    insertIntoAuthorOfDocumentTable(documentList, connection)
    insertIntoDocumentReferencesTable(documentList, connection)

  }

  private def insertIntoDocumentTable(
      documentList: List[Document],
      connection: Connection
  ): Unit = {
    val insertDocumentStatement = connection.prepareStatement(
      s"REPLACE INTO DOCUMENT (DOCUMENT_ID, TITLE, DOCUMENT_YEAR, N_CITATION, PAGE_START, PAGE_END, DOC_TYPE, PUBLISHER, VOLUME, ISSUE, DOI) VALUES (?, ?, ?,?, ?, ?,?, ?, ?,?,?)"
    )
    //      s"INSERT INTO DOCUMENT (DOCUMENT_ID, TITLE, DOCUMENT_YEAR, N_CITATION, PAGE_START, PAGE_END, DOC_TYPE, PUBLISHER, VOLUME, ISSUE, DOI) VALUES (?, ?, ?,?, ?, ?,?, ?, ?,?,?)")
    documentList.foreach(document => {
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
      insertDocumentStatement.addBatch()
    })
    insertDocumentStatement.executeBatch()
    connection.commit()
    insertDocumentStatement.close()
  }

  private def insertIntoAuthorTable(
      documentList: List[Document],
      connection: Connection
  ): Unit = {
    val documentsWithAuthor = documentList.filter(_.authors.isDefined)
    val insertAuthorStatement = connection.prepareStatement(
      s"REPLACE INTO AUTHOR (AUTHOR_ID, NAME, ORG) VALUES (?, ?, ?)"
    )
    //      s"INSERT INTO AUTHOR (AUTHOR_ID, NAME, ORG) VALUES (?, ?, ?)")

    documentsWithAuthor.foreach(document => {
      document.authors.orNull.foreach(author => {

        insertAuthorStatement.setLong(1, author.id)
        insertAuthorStatement.setString(2, author.name)
        insertAuthorStatement.setString(3, author.org.orNull)
        insertAuthorStatement.addBatch()
      })
    })
    insertAuthorStatement.executeBatch()
    connection.commit()
    insertAuthorStatement.close()
  }

  private def insertIntoAuthorOfDocumentTable(
      documentList: List[Document],
      connection: Connection
  ): Unit = {
    val documentsWithAuthor = documentList.filter(_.authors.isDefined)
    val insertAuthorOfDocumentStatement = connection.prepareStatement(
      s"REPLACE INTO AUTHOROFDOCUMENT (DOCUMENT_ID, AUTHOR_ID) VALUES (?, ?)"
    )
    //      s"INSERT INTO AUTHOROFDOCUMENT (DOCUMENT_ID, AUTHOR_ID) VALUES (?, ?)")
    documentsWithAuthor.foreach(document => {
      document.authors.orNull.foreach(author => {
        insertAuthorOfDocumentStatement.setLong(1, document.id)
        insertAuthorOfDocumentStatement.setLong(2, author.id)
        insertAuthorOfDocumentStatement.addBatch()
      })
    })

    insertAuthorOfDocumentStatement.executeBatch()
    connection.commit()
    insertAuthorOfDocumentStatement.close()
  }

  private def insertIntoDocumentReferencesTable(
      documentList: List[Document],
      connection: Connection
  ): Unit = {
    val documentsWithReference = documentList.filter(_.references.isDefined)
    val insertDocumentReferencesStatement = connection.prepareStatement(
      s"REPLACE INTO DOCUMENTREFERENCES (DOCUMENT_ID, REFERENCE_ID) VALUES (?, ?)"
    )
//          s"INSERT INTO DOCUMENTREFERENCES (DOCUMENT_ID, REFERENCE_ID) VALUES (?, ?)")
    documentsWithReference.foreach(document => {
      document.references.get.foreach(reference => {
        insertDocumentReferencesStatement.setLong(1, document.id)
        insertDocumentReferencesStatement.setLong(2, reference)
        insertDocumentReferencesStatement.addBatch()
      })

    })
    insertDocumentReferencesStatement.executeBatch()
    connection.commit()
    insertDocumentReferencesStatement.close()
  }

}
