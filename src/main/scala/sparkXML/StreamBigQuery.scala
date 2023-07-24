package sparkXML

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.stream.alpakka.google.{GoogleAttributes, GoogleSettings}
import akka.stream.alpakka.googlecloud.bigquery.InsertAllRetryPolicy
import akka.stream.alpakka.googlecloud.bigquery.model.{Dataset, Job, JobReference, JobState, QueryResponse, Table, TableDataListResponse, TableListResponse}
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.BigQuery
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.schema.BigQuerySchemas._
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.spray.BigQueryJsonProtocol.{bigQueryJsonFormat4, _}
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.{Done, NotUsed}
import HealthKitProtocolJSON._
import akka.stream.alpakka.google.auth.Credentials
import scala.collection.immutable.Seq
import scala.concurrent._

import utils.Utility._
object StreamBigQuery extends App {
  /* WIP
  implicit val settings = GoogleSettings(path = "src/main/scala/application.conf")
  val file = "data/healthkitOut/part-00051-ff483fe2-99ea-4024-8844-6461bfff78dd-c000.json"

  def jsonToClass(file: String): Array[HealthKit] = {
    val json = readFile(file)
    val jsonList: Array[String] = json.split(f"\n")
    val listOfKit: Array[HealthKit] = for (i <- jsonList) yield i.parseJson.convertTo[HealthKit]
    listOfKit
  }

  def writeToBigQuery(): Sink[Seq[HealthKitProtocolBQ.HealthKit], NotUsed] = {
    import HealthKitProtocolBQ._
    val healthKitInsertSink: Sink[Seq[HealthKit], NotUsed] = {
      BigQuery.insertAll[HealthKit]("health_kit", "hk_1", InsertAllRetryPolicy.WithDeduplication)
    }
    return healthKitInsertSink
  }



 println(writeToBigQuery())

  def checkIfJobsDone(jobReferences: Seq[JobReference]): Future[Boolean] = {
    for {
      jobs <- Future.sequence(jobReferences.map(ref => BigQuery.job(ref.jobId.get)))
    } yield jobs.forall(job => job.status.exists(_.state == JobState.Done))
  }

  val isDone: Future[Boolean] = for {
    jobs <- Source(people).via(peopleLoadFlow).runWith(Sink.seq)
    jobReferences = jobs.flatMap(job => job.jobReference)
    isDone <- checkIfJobsDone(jobReferences)
  } yield isDone


  val allDatasets: String = BigQuery.datasets.toString()
  println(allDatasets)
//  val defaultSettings: GoogleSettings = GoogleSettings()
}
*/
}
