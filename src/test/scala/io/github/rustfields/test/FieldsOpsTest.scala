package io.github.rustfields.test

import cats.implicits.*
import io.github.rustfields.field.DefaultableInstances.given
import io.github.rustfields.lang.FieldCalculusInterpreter
import org.scalatest.flatspec.AnyFlatSpec

class FieldsOpsTest extends AnyFlatSpec with FieldTest:
  given node: FieldCalculusInterpreter = this

  "Mapping a field" should "yield a new Field" in {
    compareLocally {
      val f = Field(Map(mid() -> 1, 2 -> 1))
      f.map(_+1)
    } {
      Field(Map(mid()->2, 2->2), 1)
    }
  }

  "Fields" should "be able to be used inside comprehensions" in {
    compareLocally {
      val f = Field(Map(mid() -> 1, 2 -> 1))
      val g = Field(Map(mid() -> 2, 2 -> 2))
      for {
        x <- f
        y <- g
      } yield x + y
    } {
      Field(Map(mid() -> 3, 2 -> 3), 0)
    }
  }

  "Fields" should "be combined with function fields" in {
    compareLocally {
      val f = Field(Map(mid() -> 1, 2 -> 1))
      val plusOne = (x: Int) => x + 1
      val plusTwo = (x: Int) => x + 2
      val functionField = Field(Map(mid() -> plusOne, 2 -> plusTwo), (_: Int) => 0)
      applyFunctionField(f, functionField)
    } {
      Field(Map(mid() -> 2, 2 -> 3), 0)
    }
  }

  "Fields of monoids" should "be able to be combined together" in {
    compareLocally {
      val f = Field(Map(mid() -> List(1), 2 -> List(1)))
      val g = Field(Map(mid() -> List(2), 2 -> List(2)))
      mappendFields(f, g)
    } {
      Field(Map(mid() -> List(1, 2), 2 -> List(1, 2)), List())
    }
  }
