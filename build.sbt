version := "1.0"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.datareply",
      scalaVersion := "2.12.15"
    )),
    name := "multi-tenant",
    libraryDependencies += "org.apache.avro" % "avro" % "1.11.0",
    //Compile / avroSource := (Compile / resourceDirectory).value / "avro"
  )

