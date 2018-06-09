import Common._
import Dependencies._

lazy val core = project.settings(commonSettings: _*)
  .settings(
    libraryDependencies += Avro4s,
    libraryDependencies += ScalaTest % Test,
    libraryDependencies += ScalaCheck % Test
  )

lazy val service = project.settings(commonSettings: _*)
  .dependsOn(core % "compile->compile;test->test")
  .settings(

  )

lazy val datafaker = (project in file("."))
  .aggregate(core, service)
  .settings(
    publishLocal := {},
    publish := {}
  )

crossScalaVersions := Seq("2.11.12", "2.12.6")
