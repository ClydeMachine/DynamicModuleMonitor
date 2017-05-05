# README

DynamicModuleMonitor is a simple library for your Scala packages that monitors a "loading dock" directory for new Scala scripts. When new files are added to the directory, they are executed.

The intended audience for this library consists of those who want to launch scripts via Scala code, in a concurrent fashion, from one common directory. This can alleviate pains of having to run a full package deployment just to push and execute simple scripts on your servers. This library is a proof-of-concept meant for a very particular edge case, and is far from a robust solution to launching concurrent application tasks.

Future development of this library includes but is not limited to:

 - [ ] Execute .jar files, not just .scala scripts, since most of the time your code won't be in .scala format at this point in development.
 - [ ] Incorporate more robust error handling and reporting.
 - [ ] Incorporate better control of the individual modules' threads, as they run until success/fail, or until the DynamicModuleMonitor process is stopped.

## Code Examples

Refer to the ```SandboxObject``` for some test code, and the ```loadingdockstaging``` directory for some example modules. When one of those is placed in the ```loadingdock``` directory and the monitor is running, you'll see the service execute the module and report its output.

You'll want to create an ```application.conf``` file like the example one I've made, that specifies what your monitoring directory will be. Absolute and relative paths are supported here - just make sure it includes a trailing "/" slash character.

**NOTE:** The monitored directory is intended to function like temp space. It can be quickly emptied out by adding a file with ```.cleanup``` in the name, and the monitor will delete everything in that directory. Naturally, this means the directory should be empty except for when you have modules to run!

```
// This is what your application.conf may contain. For now this is all you need in it.
loadingdock_path = "loadingdock/"

```

Your package may use the library something like the following:

```
  def example(): Unit = {

    val config: Config = ConfigFactory.load()
    val modulemonitor: DynamicModuleMonitor = new DynamicModuleMonitor(120, config)
    modulemonitor.start()

    // Block this thread to simulate code that your software will be doing in the meantime while
    // the monitor checks for and launches modules.
    Thread sleep 300 * 100

    // If you wish to stop the monitor (like if you set it to run indefinitely), you'll want stop().
    modulemonitor.stop()
  }
```

Pretty simple!