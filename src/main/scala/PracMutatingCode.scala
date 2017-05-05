package dynamicmodulemonitor

import com.typesafe.config.{Config, ConfigFactory}
import org.slf4j.LoggerFactory

/**
  * Launch the service and it will monitor for .scala files in /loadingdock/.
  * When .scala files are found, they are executed.
  * When .conf files are found, they are interpretted and written as .scala files,
  * then executed.
  */
object PracMutatingCode {
  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("Main")

  def testmonitor(): Unit = {
    val modulemonitor: DynamicModuleMonitor = new DynamicModuleMonitor(60)
    modulemonitor.start()
    Thread sleep 4000
    logger.info("Waited a few seconds.")
    modulemonitor.stop()
    logger.info("Stopped - make a change now.")
    Thread sleep 4000
    logger.info("Starting again.")
    modulemonitor.start()
    Thread sleep 4000
    logger.info("Waited a few more seconds, now stopping again.")
    modulemonitor.stop()
    Thread sleep 4000
  }

  def main(args: Array[String]): Unit = {
    val modulemonitor: DynamicModuleMonitor = new DynamicModuleMonitor(120)
    modulemonitor.start()
    Thread sleep 300 * 1000
    modulemonitor.stop()
  }
}
