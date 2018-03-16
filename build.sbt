import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.fhuertas",
      scalaVersion := s"$scalaMinorVersion",
      version := "0.1.0-SNAPSHOT",
      crossSbtVersions := Seq("2.10","2.11","2.12")
    )),
    name := "datafaker",
    libraryDependencies ++= Seq(
      avro4s,
      scalaCheck,
      typesafeConfig,
      scalaTest % Test
    )
  )
