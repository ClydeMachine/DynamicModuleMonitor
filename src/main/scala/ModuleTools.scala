package pracmutatingcode

import DirectoryTools._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import ModuleWriterTemplates._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
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

  /**
    * When a configuration file's parameters are sent this function, they are interpretted
    * and used to write out new .scala modules based on the ModuleWriterTemplates.
    */
  def modulePieces(userparam: String): String = {
    if (userparam.contains("logwriter")) {
      template_logwriter(userparam)
    } else if (userparam.contains("eventwriter")) {
      template_eventwriter(userparam)
    } else {
      ""
    }
  }

  def moduleWriter(moduleconfigfilename: String): Unit = {
    val modulename: String = s"${moduleconfigfilename.split('.')(0)}.scala"
    logger.info(s"Modulename determined to be $modulename.")
    val configcontents: String = readFile(s"$moduleconfigfilename").mkString(" ")
    logger.info(s"Config file $moduleconfigfilename contains '${configcontents}'.")
    val modulecontents = modulePieces(configcontents)
    logger.info(s"Writing file $modulename.")
    writeToDirectory(modulecontents,modulename)
    logger.info(s"moduleWriter finished.")
  }

  /**
    * Likely an unnecessary function as a restart of the service effectively fires off all module launches.
    * Will remove after confirming this is not needed.
    */
  def launchAllModules(): Unit = {
    for (modulefilename <- describeDirectory(loadingdock_path)) {
      if (!modulefilename.contains(".conf")) moduleLauncher(modulefilename)
    }
  }

  def newModuleMonitor(monitorwindow: Int = 120): Unit = {
    logger.info(s"Monitoring for new modules for $monitorwindow seconds...")
    var previousModules: ArrayBuffer[String] = ArrayBuffer()
    var currentModules: ArrayBuffer[String] = ArrayBuffer()

    for (i <- monitorwindow to 0 by -1) {
      previousModules = currentModules
      currentModules = describeDirectory(loadingdock_path).toBuffer[String].asInstanceOf[ArrayBuffer[String]]
      for (module <- currentModules) {
        if (!previousModules.contains(module) && module == ".cleanup") {
          logger.info(s".cleanup received - emptying the loadingdock.")
          emptyDirectory()
        } else if (!previousModules.contains(module) && module.contains(".scala")) {
          logger.warn(s"New module discovered: $module.")
          currentModules.append(module)

          // Launch the module as a concurrent task to allow for other modules to start.
          Future {moduleLauncher(module)}
          logger.warn(s"New module $module running concurrently.")
        } else if (!previousModules.contains(module) && module.contains(".blueprint")) {
          logger.info(s"New configuration discovered: $module.")
          currentModules.append(module)
          moduleWriter(module)
        } else if (!previousModules.contains(module)) {
          logger.info(s"File $module not a module or blueprint, ignoring.")
          currentModules.append(module)
        }
      }
      print(".")
      Thread sleep 1000
    }

    logger.info("Monitor has ended.")
  }

}
