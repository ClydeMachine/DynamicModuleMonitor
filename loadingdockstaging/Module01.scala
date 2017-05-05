import java.io.{File, FileOutputStream, PrintWriter}
import java.util.Calendar

class Module01 (
                 val name: String = "Module-01"
               ) {
  def someHandyFunction(): String = "Handy function output."

  def log(msg: String): Unit = {
    val filename_out = s"var/log/${module.name}.log"
    val writer = new PrintWriter(new FileOutputStream(new File(filename_out), true))
    writer.write(s"[${Calendar.getInstance().getTime}] $msg\n")
    writer.close()
  }
}

val module = new Module01()
module.log(s"${module.name} started!")
module.log(s"${module.name} ended!")

println(s"${module.name} finished successfully!")
