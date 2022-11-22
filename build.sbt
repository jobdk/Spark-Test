ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Informationssysteme-Praktikum-3"
  )
val h2Version = "2.1.212"
val l4jVersion = "2.0.3"
val scalatestVersion = "3.2.14"
val sprayVersion = "1.3.6"
val jedisVersion = "4.3.1"

libraryDependencies ++= Seq(
  //  l4j
  "org.slf4j" % "slf4j-api" % l4jVersion,
  "org.slf4j" % "slf4j-simple" % l4jVersion,
  // Scalatest
  "org.scalatest" %% "scalatest" % scalatestVersion % "test",
  // Spray
  "io.spray" %% "spray-json" % sprayVersion,
  // Logging
  "org.slf4j" % "slf4j-api" % l4jVersion,
  "org.slf4j" % "slf4j-simple" % l4jVersion,
  "org.apache.spark" %% "spark-core" % "3.3.1",
  "org.apache.spark" %% "spark-sql" % "3.3.1",
  "com.google.code.gson" % "gson" % "2.10"
)
