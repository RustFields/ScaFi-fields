package io.github.rustfields.test.lang

import io.github.rustfields.test.FieldTest
import io.github.rustfields.vm.Context
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers


class TestLangByRound extends AnyFunSpec with FieldTest with Matchers:
  val REPF, NBRF, BRANCHF = new ItWord

  val context: Context = ctx(0, Map())

  BRANCHF("should split the computation based on a condition") {
    val lv1 = 1; val lv2 = 2

    val res = round(context, branchf(true){lv1}{lv2}).root[Field[Int]]()
    res shouldBe Field.fromSelfValue(1)
  }

  REPF("should compute a function between rounds") {
    def program: Field[Int] = repf(7)(_+1)
    val export1 = round(context, program)
    val res = round(ctx(0, Map(mid() -> export1)), program).root[Field[Int]]()
    res shouldBe Field.fromSelfValue(9)
  }

  NBRF("nbrf") {
    def program: Field[Int] = nbrf(7)
    val context0 = ctx(0, Map())
    val context1 = ctx(1, Map())
    val export0 = round(context0, program)
    val export1 = round(context1, program)
    val context2 = ctx(0, Map(mid() -> export0, 1 -> export1))
    val res = round(context2, program).root[Field[Int]]()
    println(res)
  }