package utils

import com.google.api.gax.paging.Page
import com.google.cloud.bigquery.BigQuery
import com.google.cloud.bigquery.BigQueryException
import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.Field
import com.google.cloud.bigquery.FormatOptions
import com.google.cloud.bigquery.Job
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.LoadJobConfiguration
import com.google.cloud.bigquery.Schema
import com.google.cloud.bigquery.StandardSQLTypeName
import com.google.cloud.bigquery.TableId
import com.google.cloud.storage.{Blob, Storage, StorageOptions}
import sparkXML.GCSVariables
private trait ListObjects {
  def listObjects(projectId: String, bucketName: String): Unit = {
    // need to test
    val storage: Storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService()
    val blobs: Page[Blob] = storage.list(bucketName)
    val blobList : Seq[String] = for (i <- blobs) yield i
    return blobList
  }
}
object BigQueryTransfer extends ListObjects with GCSVariables {
  def main(args: Array[String]): Unit = {
    val sourceUris = listObjects(project, bucket)
  }
}
