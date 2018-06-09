import sbt._


object Dependencies {
  lazy val Avro4sVersion = "1.8.3"
  lazy val ScalaTestVersion = "3.0.4"
  lazy val ScalaCheckVersion = "1.13.4"


  lazy val Avro4s = "com.sksamuel.avro4s" %% "avro4s-core" % Avro4sVersion // Transitive org.apache.avro 1.8.2

  // Tests
  lazy val ScalaTest = "org.scalatest" %% "scalatest" % ScalaTestVersion
  lazy val ScalaCheck = "org.scalacheck" %% "scalacheck" % ScalaCheckVersion
}
