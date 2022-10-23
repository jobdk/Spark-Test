package Utils

object SqlCommands {
  val DROP_DOCUMENT_REFERENCES_FOREIGN_KEY: String =
    "ALTER TABLE DOCUMENTREFERENCES DROP constraint CONSTRAINT_D5;".stripMargin

  val ADD_DOCUMENT_REFERENCES_FOREIGN_KEY: String =
    """ALTER TABLE DOCUMENTREFERENCES ADD CONSTRAINT CONSTRAINT_D5
      FOREIGN KEY (DOCUMENT_ID) REFERENCES DOCUMENT(DOCUMENT_ID);
      """.stripMargin

  val DROP_AUTHOR_OF_DOCUMENT_FOREIGN_KEY: String =
    """ALTER TABLE AUTHOROFDOCUMENT
            DROP constraint CONSTRAINT_AD;
            ALTER TABLE AUTHOROFDOCUMENT
            DROP constraint CONSTRAINT_ADF;""".stripMargin

  val ADD_AUTHOR_OF_DOCUMENT_FOREIGN_KEY: String =
    """ALTER TABLE AUTHOROFDOCUMENT
                ADD CONSTRAINT CONSTRAINT_AD
                    FOREIGN KEY (DOCUMENT_ID) REFERENCES DOCUMENT(DOCUMENT_ID);
            ALTER TABLE AUTHOROFDOCUMENT
                ADD CONSTRAINT CONSTRAINT_ADF
                    FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR(AUTHOR_ID);""".stripMargin

  val CREATE_SCHEMA_STATEMENTS_WITHOUT_PRIMARY_KEY =
    """
                DROP TABLE if EXISTS authorOfDocument;
                DROP TABLE if EXISTS documentReferences;
                DROP TABLE if EXISTS document;
                DROP TABLE if EXISTS author;

                CREATE TABLE document
                (
                    document_id   LONG NOT NULL UNIQUE,
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
                    author_id LONG NOT NULL UNIQUE,
                    name      VARCHAR(300),
                    org       VARCHAR(800)
                );

                CREATE TABLE authorOfDocument
                (
                    document_id LONG NOT NULL,
                    author_id   LONG NOT NULL,
                    UNIQUE(document_id, author_id)
                );

                CREATE TABLE documentReferences
                (
                    document_id  LONG NOT NULL UNIQUE,
                    reference_id LONG NOT NULL,
                    UNIQUE(document_id, reference_id)
                );
              """.stripMargin

  val CREATE_SCHEMA_STATEMENTS_WITH_PRIMARY_KEY =
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
                    doi           VARCHAR(800),
                    PRIMARY KEY (document_id)
                );
                CREATE TABLE author
                (
                    author_id LONG NOT NULL,
                    name      VARCHAR(300),
                    org       VARCHAR(800),
                    PRIMARY KEY (author_id)
                );
                CREATE TABLE authorOfDocument
                (
                    document_id LONG NOT NULL,
                    author_id   LONG NOT NULL,
                    PRIMARY KEY (document_id, author_id),
                    FOREIGN KEY (document_id) REFERENCES document (document_id),
                    FOREIGN KEY (author_id) REFERENCES author (author_id)
                );
                CREATE TABLE documentReferences
                (
                    document_id  LONG NOT NULL,
                    reference_id LONG NOT NULL,
                    PRIMARY KEY (document_id, reference_id),
                    FOREIGN KEY (document_id) REFERENCES document (document_id)
                )
                        """.stripMargin
  val DATABASE_CONNECTION_EMBEDDED: String =
    "jdbc:h2:./src/main/resources/db/DocumentsDatabase;MODE=MYSQL"
  val DATABASE_DRIVER: String = "org.h2.Driver"

}
