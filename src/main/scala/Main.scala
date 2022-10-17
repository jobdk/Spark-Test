import model.Document
import services.DatabaseService.initialiseDatabase
import services.DocumentMapperService

import java.sql.Connection


object Main {
  def main(args: Array[String]): Unit = {
        val connection: Connection = initialiseDatabase()
        val documents: List[Document] = DocumentMapperService.mapJsonToListOfDocuments("dblp.v12.json", connection)
//        val documents: List[Document] = DocumentMapperService.mapJsonToListOfDocuments("small.json", connection)

  }
}

/*
* Aufgabe 3
* So lange wie die Serealisierung und das einlesen in die Datenbank,da sie Objekt für Objekt eingelesen wird
*
*
*
*
*
* */