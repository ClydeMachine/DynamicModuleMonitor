package pracmutatingcode

import DirectoryTools._
import org.slf4j.LoggerFactory

object ModuleTools {

  val logger = LoggerFactory.getLogger("ModuleTools")

  /** Likely make this an unordered collection of launchable Module objects. */
  def moduleLauncher(modules: Array[String]): Unit = {
    logger.info(s"Launching ${modules.length} modules.")
  }

  /** Read modules from directory,
    * interpret contents as a sequence of launchable Modules. */
  def moduleReader(): Unit = {
    logger.info(s"Current dir: ${describeWorkingDirectory()}.")

    logger.info(s"Describing $loadingdock_path.")
    val modulenames = describeDirectory(loadingdock_path)
    for(i <- modulenames) logger.info(s"Found module $i.")

    logger.info(s"Loading ${modulenames.length} modules.")
    readModuleFiles(modulenames)

    logger.info(s"main() done.")
  }

}
