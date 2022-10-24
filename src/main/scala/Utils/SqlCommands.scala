package Utils

object SqlCommands {
  final val ADD_ALL_KEYS: String =
    """
      ALTER TABLE DOCUMENT ADD CONSTRAINT CONSTRAINT_6 PRIMARY KEY (DOCUMENT_ID);
      ALTER TABLE DOCUMENTREFERENCES ADD CONSTRAINT CONSTRAINT_D PRIMARY KEY (DOCUMENT_ID, REFERENCE_ID);
      ALTER TABLE AUTHOROFDOCUMENT ADD CONSTRAINT CONSTRAINT_A PRIMARY KEY (AUTHOR_ID, DOCUMENT_ID);
      ALTER TABLE DOCUMENTREFERENCES ADD CONSTRAINT CONSTRAINT_D5 FOREIGN KEY (DOCUMENT_ID) REFERENCES DOCUMENT(DOCUMENT_ID);
      ALTER TABLE AUTHOROFDOCUMENT ADD CONSTRAINT CONSTRAINT_AD FOREIGN KEY (DOCUMENT_ID) REFERENCES DOCUMENT(DOCUMENT_ID);
      ALTER TABLE AUTHOROFDOCUMENT ADD CONSTRAINT CONSTRAINT_ADF FOREIGN KEY (AUTHOR_ID) REFERENCES AUTHOR(AUTHOR_ID);
      """.stripMargin

  final val DROP_ALL_KEYS: String =
    """
      ALTER TABLE DOCUMENTREFERENCES DROP constraint CONSTRAINT_D5;
      ALTER TABLE AUTHOROFDOCUMENT DROP constraint CONSTRAINT_AD;
      ALTER TABLE AUTHOROFDOCUMENT DROP constraint CONSTRAINT_ADF;
      ALTER TABLE DOCUMENT DROP CONSTRAINT CONSTRAINT_6;
      ALTER TABLE DOCUMENTREFERENCES DROP CONSTRAINT CONSTRAINT_D;
      ALTER TABLE AUTHOROFDOCUMENT DROP CONSTRAINT CONSTRAINT_A;
       """.stripMargin

  final val CREATE_SCHEMA_STATEMENTS_WITHOUT_PRIMARY_KEY: String =
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

  final val CREATE_SCHEMA_STATEMENTS_WITH_PRIMARY_KEY: String =
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
  final val CREATE_SCHEMA_STATEMENTS_WITH_PRIMARY_KEY_ON_AUTHOR: String =
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
                    org       VARCHAR(800),
                    PRIMARY KEY (author_id)
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
                )
                        """.stripMargin
  final val DATABASE_CONNECTION_EMBEDDED: String =
    "jdbc:h2:./src/main/resources/db/DocumentsDatabase;MODE=MYSQL"
  final val DATABASE_DRIVER: String = "org.h2.Driver"

  final val DATABASE_CLIENT_SERVER_PATH: String =
    s"jdbc:h2:tcp://localhost:9092/"
      .concat(
        "/Users/john/dev/Studium/Informationssysteme/Informationssysteme-Praktikum-1/Informationssysteme-Praktikum-1/src/main/resources/db/DocumentsDatabase;"
      )
      .concat("MODE=MYSQL")

}
