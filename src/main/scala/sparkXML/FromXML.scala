package sparkXML

import org.apache.spark.sql.SparkSession
import org.apache.log4j._
import com.databricks.spark.xml._
import utils.GCSVariables
import utils.SparkUtility._
import utils.DataMigration._

object FromXML extends GCSVariables {

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
//    root
//    |-- HeartRateVariabilityMetadataList: struct
//    (nullable = true)
//    | |-- InstantaneousBeatsPerMinute: array
//    (nullable = true)
//    | | |-- element: struct
//    (containsNull = true)
//    | | | |-- _VALUE: string
//    (nullable = true)
//    | | | |-- _bpm: long
//    (nullable = true)
//    | | | |-- _time: string
//    (nullable = true)
//    |-- MetadataEntry: array
//    (nullable = true)
//    | |-- element: struct
//    (containsNull = true)
//    | | |-- _VALUE: string
//    (nullable = true)
//    | | |-- _key: string
//    (nullable = true)
//    | | |-- _value: string
//    (nullable = true)
//    |-- _creationDate: string
//    (nullable = true)
//    |-- _device: string
//    (nullable = true)
//    |-- _endDate: string
//    (nullable = true)
//    |-- _sourceName: string
//    (nullable = true)
//    |-- _sourceVersion: string
//    (nullable = true)
//    |-- _startDate: string
//    (nullable = true)
//    |-- _type: string
//    (nullable = true)
//    |-- _unit: string
//    (nullable = true)
//    |-- _value: string
//    (nullable = true)
//    */
    df.createOrReplaceTempView("health_kit_data")
    val sqlFile = "sqlFiles/avg_by_month.sql"
    val dfByMonth = spark.sql(readFile(sqlFile))
////    val Results = dfByMonth.collect
    val avgPath = "data/healthkitOut/avg_by_month"
    dfByMonth.printSchema
    dfByMonth
      .write
      .json(s"$avgPath")

    spark.stop
    val storageClient = new StoragePal()
    val filesToWrite: List[String] = getProcessedFiles(avgPath)
    for (file <- filesToWrite) storageClient.uploadObject(project, bucket, file.split("/")(7), file)
    /*
    ^^^ Once there's more SQL files and results directories it may be cleanest
    to also iterate through result paths
     */

  }
}
