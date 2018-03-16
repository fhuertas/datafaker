package com.fhuertas.datafaker

import scala.language.implicitConversions

package object implicits {
  implicit def toOptional[T](param: T): Option[T] = Some(param)

}
