version := "1.0"
resolvers += "xyz" at "https://packages.confluent.io/maven/"

lazy val root = (project in file(".")).
  settings(
    assembly / mainClass := Some("com.datareply.multitenant.Producer"),
    assembly / assemblyJarName := "producer.jar",
    assembly / assemblyMergeStrategy := {
      case PathList(ps @ _*) if ps.last equals "module-info.class" => MergeStrategy.discard
      case x   =>
        val oldStrategy = (ThisBuild / assemblyMergeStrategy).value
        oldStrategy(x)
    },
    inThisBuild(List(
      organization := "com.datareply",
      scalaVersion := "2.12.15"
    )),
    name := "multi-tenant",
    libraryDependencies += "org.apache.avro" % "avro" % "1.11.0",
    //Compile / avroSource := (Compile / resourceDirectory).value / "avro",
    libraryDependencies += "org.apache.kafka" % "kafka-clients" % "2.6.0",
    libraryDependencies += "io.confluent" % "kafka-avro-serializer" % "6.0.5",
  )


