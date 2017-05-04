package pracmutatingcode

import DirectoryTools._
import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object ModuleTools {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("ModuleTools")

  /** Launch the file right from the loaddock_path directory. */
  def moduleLauncher(modulefilename: String): Unit = {
    logger.info(s"Module $modulefilename launching.")
    Runtime.getRuntime.exec(s"scala -nc $loadingdock_path$modulefilename")
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

  def launchAllModules(): Unit = {
    for (modulefilename <- describeDirectory(loadingdock_path)) {
      moduleLauncher(modulefilename)
    }

  }

}
