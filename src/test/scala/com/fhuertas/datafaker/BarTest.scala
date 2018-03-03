package com.fhuertas.datafaker

import org.scalatest.{Matchers, WordSpec}

class BarTest extends WordSpec with Matchers {
  "Bar" should {
    "should end" in {
      Bar.main(Array.empty[String])
    }
  }

}
