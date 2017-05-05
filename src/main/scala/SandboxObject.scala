package dynamicmodulemonitor

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

/**
  * Some test code you can play with to get familiar.
  */
object SandboxObject {
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
    Thread sleep 300 * 100
    modulemonitor.stop()
  }
}
