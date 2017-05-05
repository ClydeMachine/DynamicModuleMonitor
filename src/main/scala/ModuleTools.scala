package dynamicmodulemonitor

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory
import scala.sys.process.Process

object ModuleTools {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("ModuleTools")

  def scalaCLIPath(): String = if (System.getProperty("os.name") contains "Windows") "scala.bat" else "scala"

  /** Launch the file right from the loaddock_path directory. */
  def moduleLauncher(modulefilename: String): Unit = {
    logger.info(s"Module $modulefilename launching.")
    val process = Process(
      Seq(
        scalaCLIPath(),
        "-nc",
        s"$loadingdock_path$modulefilename"
      )
    ).lineStream_!
    for(output <- process) {
      logger.info(s"($modulefilename) $output")
    }
    logger.info(s"Module $modulefilename has finished.")
  }

}
