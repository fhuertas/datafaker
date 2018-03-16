package com.fhuertas.datafaker.gens

import com.typesafe.config.Config

class BaseValues(config: Config) {
    lazy val getBaseWords: Set[String] = ???
}

object BaseValues {
  lazy val genBaseWord: Set[String] = ???
}