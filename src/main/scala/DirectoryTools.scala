package pracmutatingcode

import scala.collection.mutable.ArrayBuffer
import java.io.File

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.mutable
import scala.io.Source

object DirectoryTools {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("DirectoryTools")

  def writeToDirectory(modulecontents: Array[String], modulename: String): Unit = {

  }

  def readFile(modulefile: String): Array[String] = {
    val filename_in = s"${loadingdock_path}${modulefile}"
    logger.info(s"Reading file $filename_in.")
    var readlines: ArrayBuffer[String] = ArrayBuffer()
    for (line <- Source.fromFile(filename_in).getLines()) readlines.append(line)
    readlines.toArray
  }

  def readModuleFiles(modulefiles: Array[String]): Unit = {
    val modules_loaded: mutable.Map[String, Array[String]] = mutable.Map()

    logger.info("Start reading in module file contents.")
    for(modulefilename <- modulefiles) {
      logger.info(s"Passing $modulefilename for reading.")
      modules_loaded += (
        modulefilename -> readFile(modulefilename)
      )
    }

    logger.info(s"Module file contents loaded! Map is ${modules_loaded.size} items large.")
  }

  def describeWorkingDirectory(): String = new File(".").getCanonicalPath

  def describeDirectory(path: String): Array[String] = {
    new File(path)
      .listFiles
      .filter(_.isFile)
      .map(_.getName)
  }

}
