package sparkXML

import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.stream.alpakka.google.{GoogleAttributes, GoogleSettings}
import akka.stream.alpakka.googlecloud.bigquery.InsertAllRetryPolicy
import akka.stream.alpakka.googlecloud.bigquery.model.{Dataset, Job, JobReference, JobState, QueryResponse, Table, TableDataListResponse, TableListResponse}
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.BigQuery
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.schema.BigQuerySchemas._
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.spray.BigQueryJsonProtocol._
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.{Done, NotUsed}
import utils.Utility._
object toBigQuery extends App {
  case class HealthKit(_type: String, unit: String, value: BigDecimal, month: String)
  private implicit val healthKitFormat = bigQueryJsonFormat4(HealthKit.apply)

  val file = "data/healthkitOut/part-00051-ff483fe2-99ea-4024-8844-6461bfff78dd-c000.json"
  def jsonToClass(file: String): Seq[HealthKit] = {
    /*
    WIP
     */
    import HealthKitProtocolJSON._

    val json = readFile(file)
    val jsonList: Seq[String] = json.split(f"\n")
    val listOfKit : Seq[HealthKit] = for (i <- jsonList) yield i.parseJson.convertTo[HealthKit]
    listOfKit
  }
  def writeToBigQuery(): = {
    /*
    WIP
     */
    import HealthKitProtocolBQ._
//    BigQuery.insertAll[HealthKit]("health_kit", "hk_1", InsertAllRetryPolicy.WithDeduplication)

  }


  println(jsonToClass(file).head.getClass)

}

