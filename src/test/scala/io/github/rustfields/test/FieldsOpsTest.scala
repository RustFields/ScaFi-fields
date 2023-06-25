package io.github.rustfields.test

import cats.implicits.*
import io.github.rustfields.lang.FieldCalculusInterpreter
import io.github.rustfields.test.FieldOps
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.scalatest.matchers.should.Matchers.shouldBe
import io.github.rustfields.test.DefaultableInstances.given

class FieldsOpsTest extends AnyFlatSpec with FieldTest with TestUtils:
  given node: FieldCalculusInterpreter = new FieldCalculusInterpreter {}

  val context = ctx(selfId = 0, exports = Map())
  // ACT
  val res = round(context, 77).root[Int]()

  "Mapping a field" should "yield a new Field" in {
    val f = Field(Map(mid() -> 1, 2 -> 1))
    f.map(_ + 1).getMap shouldBe Map(mid() -> 2, 2 -> 2)
  }

  "Fields" should "be able to be used inside comprehensions" in {
    val f = Field(Map(mid() -> 1, 2 -> 1))
    val g = Field(Map(mid() -> 2, 2 -> 2))
    val h = for {
      x <- f
      y <- g
    } yield x + y
    h.getMap shouldBe Map(mid() -> 3, 2 -> 3)
  }

  "Fields" should "be composed in a functional way" in {
    val plusOne = (x: Int) => x + 1
    val plusTwo = (x: Int) => x + 2

    val f = Field(Map(mid() -> 1, 2 -> 2, 3 -> 1, 4 -> 2, 5 -> 1))
    val g: Field[Int => Int] = Field(
      Map(
        mid() -> plusOne,
        2 -> plusTwo,
        3 -> plusOne,
        4 -> plusTwo,
        5 -> plusOne,
      ),
      (_: Int) => 0
    )
    val h = for
      x <- f
      y <- g
    yield y.apply(x)
    h.getMap shouldBe Map(mid() -> 2, 2 -> 4, 3 -> 2, 4 -> 4, 5 -> 2)

    val h1 = applyToAll(f, plusOne)
    h1.getMap shouldBe Map(mid() -> 2, 2 -> 3, 3 -> 2, 4 -> 3, 5 -> 2)

    val h2 = applyToAll(Field.lift(1), plusTwo)
    h2.default shouldBe 3
  }

  "Fields" should "be able to be applied to functions" in {
    val field = Field.lift(1)
    val plusOne = (x: Int) => x + 1

    val result = field >> plusOne >> plusOne >> plusOne
    result.default shouldBe 4
  }

  "Fields of monoids" should "be able to be combined together" in {
    val f1 = Field(Map(mid() -> 1, 2 -> 2, 3 -> 1, 4 -> 2, 5 -> 1))
    val g1 = Field(Map(mid() -> 2, 2 -> 2, 3 -> 2, 4 -> 2, 5 -> 2))
    val h1 = mappendFields(f1, g1)
    h1.getMap shouldBe Map(mid() -> 3, 2 -> 4, 3 -> 3, 4 -> 4, 5 -> 3)

    val f2 = Field(
      Map(
        mid() -> List(1),
        2 -> List(2),
        3 -> List(1),
        4 -> List(2),
        5 -> List(1),
      )
    )
    val g2 = Field(
      Map(
        mid() -> List(2),
        2 -> List(2),
        3 -> List(2),
        4 -> List(2),
        5 -> List(2),
      )
    )
    val h2 = mappendFields(f2, g2)
    h2.getMap shouldBe Map(
      mid() -> List(1, 2),
      2 -> List(2, 2),
      3 -> List(1, 2),
      4 -> List(2, 2),
      5 -> List(1, 2),
    )
  }
