
import java.io.{File, FileOutputStream, PrintWriter}

val modulename = "eventwriter"
val filename_out = s"var/log/eventwriter.events.log"
val writer = new PrintWriter(new FileOutputStream(new File(filename_out), false))
writer.write(s"eventwriter module was created then executed!")
writer.close()
println(s"eventwriter completed!")
    