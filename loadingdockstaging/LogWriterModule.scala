
import java.io.{File, FileOutputStream, PrintWriter}

val modulename = "logwriter"
val filename_out = s"var/log/logwriter.log"
val writer = new PrintWriter(new FileOutputStream(new File(filename_out), false))
writer.write(s"logwriter initialized!")
writer.close()
println(s"logwriter completed!")
    