package io.github.rustfields.test.lang

import io.github.rustfields.test.FieldTest
import io.github.rustfields.vm.Context
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.language.implicitConversions


class TestLangByRound extends AnyFunSpec with FieldTest with Matchers:
  val REPF, NBRF, BRANCHF, FOOLDHOODF = new ItWord

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
    round(context2, program).root[Field[Int]]() shouldBe Field(Map(0 -> 2),2)
  }