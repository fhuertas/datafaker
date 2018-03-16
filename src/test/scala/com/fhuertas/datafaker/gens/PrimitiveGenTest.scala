package com.fhuertas.datafaker.gens

import org.scalacheck.Gen
import org.scalatest.{Matchers, WordSpec}

class PrimitiveGenTest extends WordSpec with Matchers {


  "Primitive generators" should {
    "generate a boolean no parametrized" in {
      PrimitiveGen.genBoolean() shouldBe an[Gen[Boolean]]
    }

    "return the boolean value if it set" in {
      val value = Gen.oneOf(true, false).sample.get
      PrimitiveGen.genBoolean(Some(value)).sample.get shouldBe value
    }

    "return a no parametrized integer" in {
      PrimitiveGen.genInt() shouldBe an[Gen[Int]]
    }

    "return a parametrized value" in {
      val (min, max) = (10, 20)
      PrimitiveGen.genInt(Some(min), Some(max)).sample.get should be <= 20
      PrimitiveGen.genInt(Some(min), Some(max)).sample.get should be >= 10
      PrimitiveGen.genInt(Some(10), Some(10)).sample.get should be (10)
    }
  }
}
