ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.12.11"
ThisBuild / crossScalaVersions := Seq("2.13")
lazy val root = (project in file("."))
  .settings(
    name := "HealthExportProcessing"
  )
val AkkaVersion = "2.8.1"
val AkkaHttpVersion = "10.5.1"

ThisBuild / libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "3.0.0",
  "org.apache.spark" %% "spark-sql" % "3.0.0",
  "org.apache.spark" %% "spark-mllib" % "3.0.0",
  "org.apache.spark" %% "spark-streaming" % "3.0.0",
  "com.databricks" %% "spark-xml" % "0.16.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-google-cloud-bigquery" % "6.0.1",
  "com.lightbend.akka" %% "akka-stream-alpakka-google-common" % "6.0.1",
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-jackson" % AkkaHttpVersion,
  "com.google.cloud" % "google-cloud-storage" % "2.24.0"
)
