package sparkXML

import spray.json._
import DefaultJsonProtocol._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.stream.alpakka.google.{GoogleAttributes, GoogleSettings}
import akka.stream.alpakka.googlecloud.bigquery.InsertAllRetryPolicy
import akka.stream.alpakka.googlecloud.bigquery.model.{
  Dataset,
  Job,
  JobReference,
  JobState,
  QueryResponse,
  Table,
  TableDataListResponse,
  TableListResponse
}
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.BigQuery
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.schema.BigQuerySchemas._
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.spray.BigQueryJsonProtocol._
import akka.stream.scaladsl.{Flow, Sink, Source}
import akka.{Done, NotUsed}
import utils.Utility._
object toBigQuery extends App {
  case class HealthKit(_type: String, unit: String, value: BigDecimal, month: String)

  object HealthKitProtocol extends DefaultJsonProtocol {
    implicit object HealthKitFormat extends RootJsonFormat[HealthKit] {
      def write(obj: HealthKit) = ???

      def read(json: JsValue): HealthKit = {
        json.asJsObject.getFields("type", "unit", "value", "month") match {
          case Seq(JsString(a), JsString(b), JsNumber(c), JsString(d)) =>
            HealthKit(a, b, c, d)
          case unrecognized => deserializationError(s"json serialization error $unrecognized")
        }
      }
    }

  }
  val file = "data/healthkitOut/part-00051-ff483fe2-99ea-4024-8844-6461bfff78dd-c000.json"
  def jsonToClass(file: String): Unit = {
    import HealthKitProtocol._

    val json = readFile(file)
    val jsonList: Seq[String] = json.split(f"\n")
    val listOfKit : Seq[HealthKit] = for (i <- jsonList) yield i.parseJson.convertTo[HealthKit]
    val peopleInsertSink: Sink[Seq[HealthKit], NotUsed] =
      BigQuery.insertAll[HealthKit]("health_kit", "hk_1", InsertAllRetryPolicy.WithDeduplication)
  }



}

