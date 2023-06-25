package io.github.rustfields.test

import org.scalatest.funsuite.AnyFunSuite
import io.github.rustfields.test.DefaultableInstances.given
import io.github.rustfields.lang.FieldCalculusInterpreter
import io.github.rustfields.vm.Context
class FieldsTest extends AnyFunSuite with FieldTest with TestUtils:
  given node: FieldCalculusInterpreter = new FieldCalculusInterpreter {}

  val context: Context = ctx(selfId = 0, exports = Map())
  // ACT
  val res: Int = round(context, 77).root[Int]()

  test("Field creation") {
    val f: Field[Int] = Field(Map(mid() -> 1))
    assert(f.getMap.get(mid()).contains(1))
    assert(f.default == 0)
  }

  test("Lift a value to a field") {
    val f = Field.lift(1)
    assert(f.getMap.isEmpty)
    assert(f.default == 1)
  }
