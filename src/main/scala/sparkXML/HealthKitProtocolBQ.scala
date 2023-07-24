package sparkXML
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.spray.{BigQueryJsonProtocol, BigQueryRootJsonFormat}
import akka.stream.alpakka.googlecloud.bigquery.scaladsl.spray.BigQueryJsonProtocol._
import spray.json.{DefaultJsonProtocol, JsString, JsNumber, JsValue, RootJsonFormat, deserializationError}
/*
WIP
 */
object HealthKitProtocolBQ extends BigQueryJsonProtocol {
  case class HealthKit(_type: String, unit: String, value: BigDecimal, month: String)
  implicit object HealthKitFormatBQ extends BigQueryRootJsonFormat[HealthKit] {
    def write(obj: HealthKit) = ???
    def read(json: JsValue) = ???
  }

}
