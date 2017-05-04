import java.io.{File, FileOutputStream, PrintWriter}

class Module01 (
                 val name: String = "Module-01"
               ) {
  def someHandyFunction(): String = "Handy function output."
}

val module = new Module01()
val filename_out = s"${module.name}.log"
val writer = new PrintWriter(new FileOutputStream(new File(filename_out), true))
writer.write(s"${module.name} initialized!\n")
writer.close()
println(s"${module.name} completed!")
