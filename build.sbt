import Common._

lazy val core = project.settings(commonSettings: _*)

lazy val service = project.settings(commonSettings: _*)
  .dependsOn(core)

lazy val datafaker = (project in file("."))
  .aggregate(core, service)
  .settings(
    publishLocal := {},
    publish := {}
  )

crossScalaVersions := Seq("2.11.11", "2.12.6")
