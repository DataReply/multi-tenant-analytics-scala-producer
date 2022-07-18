addSbtPlugin("com.github.sbt" % "sbt-avro" % "3.4.0")
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "1.2.0")
addDependencyTreePlugin
// Java sources compiled with one version of Avro might be incompatible with a
// different version of the Avro library. Therefore we specify the compiler
// version here explicitly.
libraryDependencies += "org.apache.avro" % "avro-compiler" % "1.11.0"