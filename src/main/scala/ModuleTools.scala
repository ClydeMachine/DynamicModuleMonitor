package pracmutatingcode

import DirectoryTools._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import ModuleWriterTemplates._

import scala.sys.process.Process

object ModuleTools {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("ModuleTools")

  /** Launch the file right from the loaddock_path directory. */
  def moduleLauncher(modulefilename: String): Unit = {
    logger.info(s"Module $modulefilename launching.")
    val process = Process(s"scala -nc $modulefilename")
    logger.info(s"Module $modulefilename should now have executed!")
  }

  /** Read modules from directory,
    * interpret contents as a sequence of launchable Modules. */
  def moduleReader(): Map[String, Array[String]] = {
    logger.info(s"Current dir: ${describeWorkingDirectory()}.")

    logger.info(s"Describing $loadingdock_path.")
    val modulenames = describeDirectory(loadingdock_path)
    for(i <- modulenames) logger.info(s"Found module $i.")

    logger.info(s"Loading ${modulenames.length} modules.")

    /**
      * We *can* load these modules as String Arrays, but we don't need to - improve this to launch them one at a time.
      */
    readModuleFiles(modulenames)
  }

  /**
    * When a configuration file's parameters are sent this function, they are interpretted
    * and used to write out new .scala modules based on the ModuleWriterTemplates.
    *
    * @param userparam
    * @return
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
          logger.info(s"New module discovered: $module.")
          currentModules.append(module)
          moduleLauncher(module)
        } else if (!previousModules.contains(module) && module.contains(".conf")) {
          logger.info(s"New configuration discovered: $module.")
          currentModules.append(module)
          moduleWriter(module)
        } else if (!previousModules.contains(module)) {
          logger.info(s"File $module not a module or config, ignoring.")
          currentModules.append(module)
        }
      }
      print(".")
      Thread sleep 1000
    }

    logger.info("Monitor has ended.")
  }

}
