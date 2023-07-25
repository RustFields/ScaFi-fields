package io.github.rustfields.test.lang

import io.github.rustfields.test.FieldTest
import io.github.rustfields.vm.Slot.*
import io.github.rustfields.vm.Path.*
import io.github.rustfields.vm.{Context, Export}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import org.scalactic.Prettifier.default
import scala.language.implicitConversions
import cats.implicits.*
import io.github.rustfields.field.lang.FieldLib
import io.github.rustfields.field.syntax.FieldSyntax


class TestLangByRound extends AnyFunSpec with FieldTest with FieldLib with FieldSyntax with Matchers:
  val REPF, NBRF, BRANCHF, FOLDHOODF, Alignment, Exports, Nesting = new ItWord

  val context: Context = ctx(0, Map())

  BRANCHF("should split the computation based on a condition") {
    val lv1 = 1
    val lv2 = 2
    round(context, branchf(true){lv1}{lv2}).root[Field[Int]]() shouldBe Field.fromSelfValue(lv1)
    round(context, branchf(false){lv1}{lv2}).root[Field[Int]]() shouldBe Field.fromSelfValue(lv2)
  }

  REPF("should compute a function between rounds") {
    def program: Field[Int] = repf(7)(_ + 1)
    val exp = round(context, program)
    round(ctx(0, Map(mid() -> exp)), program).root[Field[Int]]() shouldBe Field.fromSelfValue(9)
  }

  NBRF("nbrf") {
    def program: Field[Int] = nbrf(1 + 1)
    val context0 = ctx(0, Map())
    val context1 = ctx(1, Map())
    val export0 = round(context0, program)
    val export1 = round(context1, program)
    val context2 = ctx(0, Map(0 -> export0, 1 -> export1))
    round(context2, program).root[Field[Int]]() shouldBe
      Field(Map(0 -> 2, 1 -> 2), 2)
  }

  FOLDHOODF("foldhoodf") {
    def program: Int = foldhoodf[Int](_ + _)(nbrf(2+3))
    val context0 = ctx(0)
    val context1 = ctx(1)
    val export0 = round(context0, program)
    val export1 = round(context1, program)
    val context2 = ctx(0, Map(0 -> export0, 1 -> export1))
    val export00 = round(context2, program)
    export00.root[Int]() shouldBe 15
  }

  NBRF("needs not to be nested into fold") {
    def program: Field[Int] = nbrf(1)
    // ARRANGE
    val ctx1 = ctx(0)
    // ACT + ASSERT
    round(ctx1, program).root[Field[Int]]() shouldBe Field(Map(0 -> 1), 1)
  }

  Alignment("should support interaction only between structurally compatible devices") {
    // ARRANGE
    def program: Field[Int] = repf(Field.fromSelfValue(0))(x => nbrf(x.selfValue + 1))
    val ctx1 = ctx(0)
    // ACT + ASSERT (no neighbor is aligned)
    round(ctx1, program).root[Field[Int]]() shouldBe Field(Map(0 -> 1), 1)
    // ARRANGE
    val exp = Map(1 -> Export(Rep(0) -> 1, Rep(0) / Nbr(0) -> 1))
    val ctx2 = ctx(0, exp)
    // ACT + ASSERT (one neighbor is aligned)
    round(ctx2, program).root[Field[Int]]() shouldBe Field(Map(0 -> 1, 1 -> 1), 1)
  }

  Exports("should compose") {
    val ctx1 = ctx(0, Map(), Map("sensor" -> 5))
    def expr1: Field[Int] = Field.fromSelfValue(1)
    def expr2: Field[Int] = repf(Field.fromSelfValue(7))(f => {
      for
        x <- f
        y <- Field.fromSelfValue(1)
      yield x + y
    })
    def expr3: Field[Int] = nbrf(sense[Int]("sensor"))
    /* Given expr 'e' produces exports 'o'
     * What exports are produced by 'e + e + e + e' ?
     */
    def fieldExprComp(expr: => Field[Int]): Field[Int] =
      val f1: Field[Int] = expr
      val f2: Field[Int] = expr
      val f3: Field[Int] = expr
      val f4: Field[Int] = expr
      for
        x <- f1
        y <- f2
        z <- f3
        w <- f4
      yield x + y + z + w

    round(ctx1, fieldExprComp(expr1)) shouldEqual
      Export(/ -> Field(Map(0 -> 4), 4))
    round(ctx1, fieldExprComp(expr2)) shouldEqual
      Export(
        / -> Field(Map(0 -> 32), 32),
        Rep(0) -> Field(Map(0 -> 8), 8),
        Rep(1) -> Field(Map(0 -> 8), 8),
        Rep(2) -> Field(Map(0 -> 8), 8),
        Rep(3) -> Field(Map(0 -> 8), 8),
      )
    round(ctx1, fieldExprComp(expr3)) shouldEqual
      Export(
        / -> Field(Map(0 -> 20), 20),
        Nbr(0) -> Field(Map(0 -> 5), 5),
        Nbr(1) -> Field(Map(0 -> 5), 5),
        Nbr(2) -> Field(Map(0 -> 5), 5),
        Nbr(3) -> Field(Map(0 -> 5), 5),
      )

    /* Given expr 'e' produces exports 'o'
     * What exports are produced by 'rep(0){ rep(o){ e } }' ?
     */
    def expr4: Field[Int] = repf(Field.fromSelfValue(0))(_ => repf(Field.fromSelfValue(0))(_ => expr1))
    def expr5: Field[Int] = repf(Field.fromSelfValue(0))(_ => repf(Field.fromSelfValue(0))(_ => expr2))
    def expr6: Field[Int] = repf(Field.fromSelfValue(0))(_ => repf(Field.fromSelfValue(0))(_ => expr3))
    round(ctx1, expr4) shouldEqual
      Export(
        / -> Field(Map(0 -> 1), 1),
        Rep(0) -> Field(Map(0 -> 1), 1),
        Rep(0) / Rep(0) -> Field(Map(0 -> 1), 1)
      )
    round(ctx1, expr5) shouldEqual
      Export(
        / -> Field(Map(0 -> 8), 8),
        Rep(0) -> Field(Map(0 -> 8), 8),
        Rep(0) / Rep(0) -> Field(Map(0 -> 8), 8),
        Rep(0) / Rep(0) / Rep(0) -> Field(Map(0 -> 8), 8)
      )
    round(ctx1, expr6) shouldEqual
      Export(
        / -> Field(Map(0 -> 5), 5),
        Rep(0) -> Field(Map(0 -> 5), 5),
        Rep(0) / Rep(0) -> Field(Map(0 -> 5), 5),
        Rep(0) / Rep(0) / Nbr(0) -> Field(Map(0 -> 5), 5),
      )

    /* Testing more NBRs
     */
    def expr7: Field[Int] = nbrf(sense[Int]("sensor")) + nbrf(sense[Int]("sensor"))
    round(ctx1, expr7) shouldEqual
      Export(
        / -> Field(Map(0 -> 10), 10),
        Nbr(0) -> Field(Map(0 -> 5), 5),
        Nbr(1) -> Field(Map(0 -> 5), 5),
      )
  }

  NBRF("should support interaction between aligned devices") {
    def program: Field[Int] = for x <- nbrf(mid()) yield if (x == mid()) 0 else 1
    // ARRANGE
    val exp1 = Map(
      1 -> Export(/ -> "any", Nbr(0) -> 1),
      2 -> Export(/ -> "any", Nbr(0) -> 2)
    )
    val ctx1 = ctx(0, exp1)
    // ACT
    val res1 = round(ctx1, program)
    // ASSERT
    res1.root[Field[Int]]() shouldBe Field(Map(0 -> 0, 1 -> 1, 2 -> 1), 0)
    res1.get(/(Nbr(0))) shouldBe Some(Field(Map(0 -> 0, 1 -> 1, 2 -> 2), 0))
  }

  REPF("should support dynamic evolution of fields") {
    def program: Field[Int] = repf(Field.fromSelfValue(9))(f => for x <- f yield x * 2)
    // ARRANGE
    val ctx1 = ctx(0)
    // ACT
    val exp1 = round(ctx1, program)
    // ASSERT (use initial value)
    exp1.root[Field[Int]]() shouldBe Field(Map(0 -> 18), 18)
    exp1.get[Field[Int]](/ (Rep(0))) shouldBe Some(Field(Map(0 -> 18), 18))

    // ARRANGE
    val exp = Map(0 -> Export(Rep(0) -> Field(Map(0 -> 7), 7)))
    val ctx2 = ctx(0, exp)
    // ACT
    val exp2 = round(ctx2, program)
    // ASSERT (build upon previous state)
    exp2.root[Field[Int]]() shouldBe Field(Map(0 -> 14), 14)
    exp2.get[Field[Int]](/ (Rep(0))) shouldBe Some(Field(Map(0 -> 14), 14))
  }

  BRANCHF("should support domain restriction, thus affecting the structure of exports") {
    // ARRANGE
    def program: Field[Int] =
      repf(Field.fromSelfValue(0))(f => {
        branchf(Field.fromSelfValue(for x <- f yield x % 2 == 0))(Field.fromSelfValue(7))(repf(Field.fromSelfValue(4))(_ => Field.fromSelfValue(4)))
        for x <- f yield x + 1
      })
    // ACT
    val exp = round(ctx(0), program)
    // ASSERT
    exp.root[Field[Int]]() shouldBe Field(Map(0 -> 1), 1)
    // ACT
    val ctx2 = ctx(0, Map(0 -> Export(Rep(0) -> Field(Map(0 -> 1), 1))))
    val exp2 = round(ctx2, program)
    exp2.root[Field[Int]]() shouldBe Field(Map(0 -> 2), 2)
  }
