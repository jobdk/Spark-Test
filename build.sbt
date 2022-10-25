ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.0"

lazy val root = (project in file("."))
  .settings(
    name := "Informationssysteme-Praktikum-2"
  )
val h2Version = "2.1.212"
val l4jVersion = "2.0.3"
val scalatestVersion = "3.2.14"
val sprayVersion = "1.3.6"
val jedisVersion = "4.3.1"

libraryDependencies ++= Seq(
  // H2 Database
  "com.h2database" % "h2" % h2Version,
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
  "redis.clients" % "jedis" % jedisVersion
)
