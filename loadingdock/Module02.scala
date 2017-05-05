import java.io.{File, FileOutputStream, PrintWriter}

class Module02 (
                 val name: String = "Module-02"
               ) {
  def someHandyFunction(): String = "Handy function output."
}

val module = new Module01()
val filename_out = s"/var/log/${module.name}.log"
val writer = new PrintWriter(new FileOutputStream(new File(filename_out), true))
writer.write(s"[${val now = Calendar.getInstance().getTime()}] ${module.name} initialized!\n")
writer.close()
println(s"${module.name} completed!")

