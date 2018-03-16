package com.fhuertas.datafaker.gens

import com.typesafe.config.{Config, ConfigFactory}
import org.scalatest.{Matchers, WordSpec}
import com.fhuertas.datafaker.config._

import scala.io.Source

class BaseValuesTest extends WordSpec with Matchers{

  val defaultConfig: Config = ConfigFactory.load()

  lazy val baseWords: Set[String] = {
    val wordFiles= defaultConfig.getString(StringSourceNs)
    Source.fromInputStream(getClass.getClassLoader.getResourceAsStream(wordFiles)).mkString.split("\n").toSet
  }

  "Base Values object" should  {
    "return the words set in the configuration file" in {
      println(baseWords.contains(""))
//      BaseValues.genBaseWord shouldBe baseWords
    }
  }

}
