import sbt._


object Dependencies {
  val scalaMayorVersion = "2.12"
  val scalaMinorVersion = s"$scalaMayorVersion.4"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
}
