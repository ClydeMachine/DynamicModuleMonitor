package pracmutatingcode

import com.typesafe.config.ConfigFactory
import DirectoryTools._
import ModuleTools._
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PracMutatingCode {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("Main")

  def main(args: Array[String]): Unit = {
    moduleReader()

  }
}
