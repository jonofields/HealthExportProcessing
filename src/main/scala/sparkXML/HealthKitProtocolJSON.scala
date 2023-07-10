package sparkXML

import spray.json.{DefaultJsonProtocol, JsString, JsNumber, JsValue, RootJsonFormat, deserializationError}
/*
WIP
 */
object HealthKitProtocolJSON extends DefaultJsonProtocol {
  case class HealthKit(_type: String, unit: String, value: BigDecimal, month: String)

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

