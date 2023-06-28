package io.github.rustfields.test.lang

import io.github.rustfields.test.{FieldTest, TestUtils}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


class LangTest extends AnyFlatSpec with FieldTest with TestUtils with Matchers:
  

  "Local values" should "be evaluated as Fields" in {
    val lv1 = 1; val lv2 = 2
    val context = ctx(0, Map())
    val res = round(context, branchf(true){lv1}{lv2}).root[Field[Int]]()
    res shouldBe Field.lift(1)
  }