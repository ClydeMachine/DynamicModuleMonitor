package pracmutatingcode

import com.typesafe.config.ConfigFactory
import DirectoryTools._
import ModuleTools._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
  * Launch the service and it will monitor for .scala and .conf files in /loadingdock/.
  * When .scala files are found, they are executed.
  * When .conf files are found, they are interpretted and written as .scala files,
  * then executed.
  */
object PracMutatingCode {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("Main")

  def main(args: Array[String]): Unit = {
    // Launch the service for x number of seconds.
    newModuleMonitor(120)
  }
}
