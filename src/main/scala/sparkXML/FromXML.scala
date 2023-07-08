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
      .xml("data/test.xml")

    df.printSchema

    df.createOrReplaceTempView("TestView")
    val avgBMI = spark.sql("SELECT AVG(_value) " +
      "FROM TestView WHERE _type = 'HKQuantityTypeIdentifierBodyMassIndex'")

    val results = avgBMI.collect
    results.foreach(println)

    spark.stop
  }
}
