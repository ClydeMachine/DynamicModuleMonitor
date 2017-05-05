# README

This is a practice project for writing self-mutating code. It'll start with basic functions of reading and writing files containing Scala code (that is to say, just some basic scripts, no dynamic loading of classes and such).

Ultimately the code will be able to do something like the following:

- The code base will launch with a generic superclass and at least one child class that extends it.
- Separately from the code base, we'll create another child class, that will be saved into the loadingdock/ directory, and pulled in as a dynamically-loaded class that can then be used to create new instances.
- To expand on this, we'll also introduce the ability to create new child classes on the fly with some user-created config files (like a JSON or YAML file), and the code base will write a .scala file containing the new child class. The codebase will then dynamically load in and use this childclass, as before.

This will satisfy the criteria for what I call self-mutating code.

## Checklist

1. Project looks to a directory for modules.
1. Found modules are loaded.
1. Loaded modules are executed.
1. Code that takes a user's parameter input from a file in that directory, and creates a module on the fly.
1. That module is written to the directory, and thanks to steps 1-3, it launches.
1. If file named ".cleanup", empty the loadingdock directory of all modules.
1. Change how configs work: If a module is found, check if a config is present. If no config, run it like normal.
1. Make blueprint files "x.blueprint" so they don't get confused with .confs.
1. If a module/config pair are found, the config should describe how the module is to be run (i.e. start a Future for it and run x times at y interval, output results to z file.)
1. Make a simple superclass and child.
1. Make a module that creates a child extending that superclass.
1. Make that module usable in the code (might be tricky without knowing the name of the child class)
1. Make the package into a reusable library.
