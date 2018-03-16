package com.fhuertas.datafaker.gens

import org.scalacheck.Gen

object PrimitiveGen {
  def genInt(min: Option[Int] = None, max: Option[Int] = None): Gen[Int] =
    Gen.choose(min.getOrElse(Int.MinValue), max.getOrElse(Int.MaxValue))


  def genBoolean(parametrized: Option[Boolean] = None): Gen[Boolean] = {
    parametrized match {
      case Some(value) => value
      case _ => Gen.oneOf(true, false)
    }
  }
}
