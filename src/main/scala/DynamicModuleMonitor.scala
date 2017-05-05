package dynamicmodulemonitor

import DirectoryTools._
import ModuleTools._
import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class DynamicModuleMonitor (
                           val monitorwindow: Int = 120,
                           val config: Config = ConfigFactory.load()
                           ) {
  val logger = LoggerFactory.getLogger("DynamicModuleMonitor")
  logger.info(s"Monitor directory set to: ${config.getString("loadingdock_path")}")

  private var iteration = monitorwindow
  val loadingdock_path = config.getString("loadingdock_path")

  def start(): Unit = {
    this.iteration = monitorwindow
    if (monitorwindow > 0) {
      logger.warn(s"Monitor started, will run for ${monitorwindow/60} minutes.")
    } else {
      logger.warn(s"Monitor started, will run indefinitely.")
    }
    Future {newModuleMonitor(monitorwindow)}
  }

  def stop(): Unit = {
    this.iteration = -1
    logger.warn(s"Monitor stopped. Background tasks may continue to run.")
  }

  def newModuleMonitor(monitorwindow: Int): Unit = {
    logger.info(s"Monitoring for new modules for $monitorwindow seconds...")
    var previousModules: ArrayBuffer[String] = ArrayBuffer()
    var currentModules: ArrayBuffer[String] = ArrayBuffer()

    while (this.iteration > -1) {
      if (monitorwindow != 0) this.iteration -= 1

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
