package utils

import com.google.api.gax.paging.Page
import com.google.cloud.storage._

import java.io.File
import java.nio.file.Paths
import scala.collection.JavaConversions._
import com.google.cloud.bigquery.{BigQuery, BigQueryException, BigQueryOptions, Field, FormatOptions, Job, JobInfo, LoadConfiguration, LoadJobConfiguration, Schema, StandardSQLTypeName, TableId}

object DataMigration extends App{
  /*
  Transfers JSON files is Spark job is processed locally, exports XML file if
  Spark job is to be executed in DataProc
   */
  class StoragePal extends GCSVariables {
    private val storage: Storage = StorageOptions.newBuilder().setProjectId(project).build().getService()

    def listObjects: Iterable[String] = {
      val blobs: Page[Blob] = storage.list(bucket)
      val iterBlobs = blobs.iterateAll()
      val scalaBlobs = iterBlobs.map(blob => blob.getName)
      scalaBlobs
    }

    def composeUri(bucketName: String, blobName: String): String = s"gs://${bucketName}/${blobName}"

    def uploadObject(project: String, bucket: String, `object`: String, filePath: String): Unit = {
      val blobId: BlobId = BlobId.of(bucket, `object`)
      val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId).build
      storage.createFrom(blobInfo, Paths.get(filePath))

      println(s"${filePath} has been written as ${`object`} to ${bucket}.")
    }
  }
  class BigQueryPal extends GCSVariables {
    private val bigquery = BigQueryOptions.getDefaultInstance().getService()
    private val schema: Schema = Schema.of(
      Field.of("type", StandardSQLTypeName.STRING),
      Field.of("unit", StandardSQLTypeName.STRING),
      Field.of("value", StandardSQLTypeName.FLOAT64),
      Field.of("month", StandardSQLTypeName.STRING),
    )
    def loadFromGCS(dataset: String = dataset, table: String = table,
                    sourceUri: String, schema: Schema = this.schema): Unit = {
      try {
        val tableId = TableId.of(dataset, table)
        val loadConfig: LoadJobConfiguration = LoadJobConfiguration
          .newBuilder(tableId, sourceUri)
          .setFormatOptions(FormatOptions.json)
          .setSchema(schema)
          .build()

        val job: Job = this.bigquery.create(JobInfo.of(loadConfig))
        job.waitFor()
        if (job.isDone) println("Data Loaded Successfully!")
        else println(s"Job failed with exception ${job.getStatus.getError}")
    }
      catch {
        case e: BigQueryException => println(s"${e.toString}")
        case e: InterruptedException => println(s"${e.toString}")
      }
    }
  }

  def getProcessedFiles(dirPath: String): List[String] = {
    val file = new File(dirPath)
    file.listFiles
      .filter(_.isFile)
      .filter(_.getName.endsWith(".json"))
      .map(_.getPath).toList
  }
  val storagePal = new StoragePal()
  val bqPal = new BigQueryPal()
  val uris = storagePal.listObjects.map(obj => storagePal.composeUri(storagePal.bucket, obj))

  for (i <- uris) bqPal.loadFromGCS(sourceUri = i)
}

