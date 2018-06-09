import sbt._
import Keys._

object Common {

  lazy val ModulesPrefix = "datafaker"

  lazy val ScalaVersion = "2.12.6"

  lazy val Version = "1.0.0-SNAPSHOT"

  def commonSettings: Seq[Def.Setting[_]] = Seq(
    organization := "com.fhuertas",
    scalaVersion := "2.12.6",
    crossScalaVersions := Seq("2.11.11", "2.12.6"),
    version := Version
  )

}
