import sbt._


object Dependencies {
  lazy val scalaMayorVersion = "2.12"
  lazy val scalaMinorVersion = s"$scalaMayorVersion.4"

  lazy val avro4sVersion = "1.8.3"
  lazy val scalaTestVersion = "3.0.4"
  lazy val scalaCheckVersion = "1.13.5"
  lazy val typesafeConfigVersion = "1.3.3"

  lazy val avro4s = "com.sksamuel.avro4s" %% "avro4s-core" % avro4sVersion // Transitive org.apache.avro 1.8.2
  lazy val scalaCheck = "org.scalacheck" %% "scalacheck" % scalaCheckVersion
//  lazy val typesafeConfig = "org.typesafe" % "config" % typesafeConfigVersion
  lazy val typesafeConfig = "com.typesafe" % "config" % "1.3.3"

  // Tests
  lazy val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion
}
