package sparkXML

import com.google.cloud.storage._
import java.nio.file.Paths
import java.io.File
object StorageTransfer extends App {
  /*
  Transfers JSON files is Spark job is processed locally, exports XML file if
  Spark job is to be executed in DataProc
   */
  def uploadObject(project: String, bucket: String, `object`: String, filePath: String): Unit = {
    val storage: Storage = StorageOptions
      .newBuilder
      .setProjectId(project)
      .build
      .getService

    val blobId: BlobId = BlobId.of(bucket, `object`)
    val blobInfo: BlobInfo = BlobInfo.newBuilder(blobId).build

    storage.createFrom(blobInfo, Paths.get(filePath))
    println(s"${filePath} has been written as ${`object`} to ${bucket}.")
  }

  def writeHeathFiles(dirPath: String): List[String] = {
    val file = new File(dirPath)
    file.listFiles
      .filter(_.isFile)
      .filter(_.getName.endsWith(".json"))
      .map(_.getPath).toList
  }

  val fileList: List[String] = writeHeathFiles("/Users/jonofields/IdeaProjects/HealthExportProcessing/data/healthkitOut")
  for (i <- fileList) uploadObject("pipeline-builds", "posted_json_test", i.split("/")(7), i)
}

