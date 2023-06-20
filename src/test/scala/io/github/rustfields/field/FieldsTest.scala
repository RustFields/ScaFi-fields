package io.github.rustfields.field

import org.scalatest.funsuite.AnyFunSuite
import io.github.rustfields.field.DefaultableInstances.given

class FieldsTest extends AnyFunSuite with FieldTest:
  test("Field creation") {
    val f: Field[Int] = Field(Map(mid -> 1))
    assert(f.getMap.get(mid).contains(1))
    assert(f.default == 0)
  }

  test("Lift a value to a field") {
    val f = Field.lift(1)
    assert(f.getMap.isEmpty)
    assert(f.default == 1)
  }
