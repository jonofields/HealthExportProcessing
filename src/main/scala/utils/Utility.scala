package utils
import scala.io.Source._
import scala.xml.XML
object Utility {
  def readFile(file: String): String = {
    val source = fromFile(file)
    val avgSql = try source.mkString finally source.close()
    avgSql
  }
  def getRoot: String = {
    val xFile = XML.loadFile("data/test.xml")
    val root = xFile.head.label
    root
  }

}
