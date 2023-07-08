package sparkXML

import org.apache.spark.sql.SparkSession
import org.apache.log4j._
import com.databricks.spark.xml._
import scala.xml

object FromXML {

  def getRoot: String = {
    val xFile = xml.XML.loadFile("data/test.xml")
    val root = xFile.head.label
    root
  }
  def main(args: Array[String]): Unit = {
    Logger.getLogger("org").setLevel(Level.ERROR)
    val spark = SparkSession
      .builder()
      .appName("SparkSQL")
      .config("spark.master", "local[*]")
      .getOrCreate()

    val df = spark.read
      .option("inferSchema", "true")
      .option("rowTag", "Record")
      .option("rootTag", getRoot)
      .xml("data/export.xml")

    /*
    df.printSchema
    root
    |-- HeartRateVariabilityMetadataList: struct
    (nullable = true)
    | |-- InstantaneousBeatsPerMinute: array
    (nullable = true)
    | | |-- element: struct
    (containsNull = true)
    | | | |-- _VALUE: string
    (nullable = true)
    | | | |-- _bpm: long
    (nullable = true)
    | | | |-- _time: string
    (nullable = true)
    |-- MetadataEntry: array
    (nullable = true)
    | |-- element: struct
    (containsNull = true)
    | | |-- _VALUE: string
    (nullable = true)
    | | |-- _key: string
    (nullable = true)
    | | |-- _value: string
    (nullable = true)
    |-- _creationDate: string
    (nullable = true)
    |-- _device: string
    (nullable = true)
    |-- _endDate: string
    (nullable = true)
    |-- _sourceName: string
    (nullable = true)
    |-- _sourceVersion: string
    (nullable = true)
    |-- _startDate: string
    (nullable = true)
    |-- _type: string
    (nullable = true)
    |-- _unit: string
    (nullable = true)
    |-- _value: string
    (nullable = true)
    */

    df.createOrReplaceTempView("TestView")
    val dfOut = spark.sql(
      "SELECT _type as type, _unit as unit, " +
      "AVG(_value) as value, date_format(_endDate, 'y-M') as month " +
      "FROM TestView " +
      "WHERE _type like 'HKQuantityTypeIdentifier%' " +
      "GROUP BY date_format(_endDate, 'y-M'), _type, _unit " +
      "ORDER BY date_format(_endDate, 'y-M') ASC"
    )

    val Results = dfOut.collect

    dfOut.printSchema
    dfOut
      .write
      .json("data/healthkit_out.json")

    spark.stop
  }
}
