package pracmutatingcode

import scala.collection.mutable.ArrayBuffer
import java.io.{File, FileOutputStream, PrintWriter}

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.io.Source

object DirectoryTools {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("DirectoryTools")

  def writeToDirectory(modulecontents: String, modulename: String): Unit = {
    val filename_out = s"${loadingdock_path}${modulename}"
    val writer = new PrintWriter(new FileOutputStream(new File(filename_out), true))
    writer.write(modulecontents)
    writer.close()
    logger.info(s"$modulename written!")
  }

  def readFile(modulefile: String): Array[String] = {
    val filename_in = s"${loadingdock_path}${modulefile}"
    logger.info(s"Reading file $filename_in.")
    var readlines: ArrayBuffer[String] = ArrayBuffer()
    for (line <- Source.fromFile(filename_in).getLines()) readlines.append(line)
    readlines.toArray
  }

  def readModuleFiles(modulefiles: Array[String]): Map[String, Array[String]] = {
    val module_contents: mutable.Map[String, Array[String]] = mutable.Map()

    logger.info("Start reading in module file contents.")
    for(modulefilename <- modulefiles) {
      logger.info(s"Passing $modulefilename for reading.")
      module_contents += (
        modulefilename -> readFile(modulefilename)
      )
    }

    logger.info(s"Module file contents loaded! Map is ${module_contents.size} items large.")
    module_contents.toMap[String, Array[String]]
  }

  def describeWorkingDirectory(): String = new File(".").getCanonicalPath

  def describeDirectory(path: String): Array[String] = {
    new File(path)
      .listFiles
      .filter(_.isFile)
      .map(_.getName)
  }

}
