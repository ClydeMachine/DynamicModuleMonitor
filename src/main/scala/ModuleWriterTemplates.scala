package dynamicmodulemonitor

import com.typesafe.config.ConfigFactory
import org.slf4j.LoggerFactory

object ModuleWriterTemplates {

  val loadingdock_path = ConfigFactory.load().getString("loadingdock_path")
  val logger = LoggerFactory.getLogger("ModuleTools")

  def template_logwriter(userparam: String): String = {
    s"""
      |import java.io.{File, FileOutputStream, PrintWriter}
      |
      |val modulename = "${userparam}"
      |val filename_out = s"var/log/${userparam}.log"
      |val writer = new PrintWriter(new FileOutputStream(new File(filename_out), false))
      |writer.write(s"${userparam} initialized!")
      |writer.close()
      |println(s"${userparam} completed!")
    """.stripMargin
  }

  def template_eventwriter(userparam: String): String = {
    s"""
       |import java.io.{File, FileOutputStream, PrintWriter}
       |
       |val modulename = "${userparam}"
       |val filename_out = s"var/log/${userparam}.events.log"
       |val writer = new PrintWriter(new FileOutputStream(new File(filename_out), false))
       |writer.write(s"${userparam} module was created then executed!")
       |writer.close()
       |println(s"${userparam} completed!")
    """.stripMargin
  }
}
