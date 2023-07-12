package io.github.rustfields.test.lang

import io.github.rustfields.test.FieldTest
import io.github.rustfields.vm.Slot.*
import io.github.rustfields.vm.Path.*
import io.github.rustfields.vm.{Context, Export}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import scala.language.implicitConversions
import cats.implicits.*


class TestLangByRound extends AnyFunSpec with FieldTest with Matchers:
  val REPF, NBRF, BRANCHF, FOLDHOODF, Alignment, Exports = new ItWord

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

  FOLDHOODF("foldhoodf"){
    def program: Field[Int] = foldhoodf(Field.lift(0))((x, _) => x + 1)
    val context0 = ctx(0, Map())
    round(context0, program).root[Field[Int]]() shouldBe Field(Map(0 -> 1), 0)
  }

  Alignment("should support interaction only between structurally compatible devices") {
    // ARRANGE
    def program: Field[Int] = repf(Field.lift(0))(x => nbrf(x + Field.lift(1)))
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
    def expr1 = Field.fromSelfValue(1)
    def expr2 = repf(Field.fromSelfValue(7))(f => {
      for
        x <- f
        y <- Field.fromSelfValue(1)
      yield x + y
    })
    def expr3 = foldhoodf(nbrf(Field.fromSelfValue(sense[Int]("sensor"))))((a, b) => {
      for
        x <- a
        y <- b
      yield x + y
    })
    /* Given expr 'e' produces exports 'o'
     * What exports are produced by 'e + e + e + e' ?
     */
    def exprComp(expr: => Field[Int]): Field[Int] =
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

    round(ctx1, exprComp(expr1)) shouldEqual
      Export(/ -> Field(Map(0 -> 4), 4))
    round(ctx1, exprComp(expr2)) shouldEqual
      Export(
        / -> Field(Map(0 -> 32), 32),
        Rep(0) -> Field(Map(0 -> 8), 8),
        Rep(1) -> Field(Map(0 -> 8), 8),
        Rep(2) -> Field(Map(0 -> 8), 8),
        Rep(3) -> Field(Map(0 -> 8), 8),
      )
    // TODO: CURRENTLY NOT WORKING
/*    round(ctx1, exprComp(expr3)) shouldEqual
      Export(
        / -> Field(Map(0 -> 20), 20),
        FoldHood(0) / Nbr(0) -> Field(Map(0 -> 5), 5),
        FoldHood(1) / Nbr(0) -> Field(Map(0 -> 5), 5),
        FoldHood(2) / Nbr(0) -> Field(Map(0 -> 5), 5),
        FoldHood(3) / Nbr(0) -> Field(Map(0 -> 5), 5),
        FoldHood(0) -> Field(Map(0 -> 5), 5),
        FoldHood(1) -> Field(Map(0 -> 5), 5),
        FoldHood(2) -> Field(Map(0 -> 5), 5),
        FoldHood(3) -> Field(Map(0 -> 5), 5)
      )*/

    /* Given expr 'e' produces exports 'o'
     * What exports are produced by 'rep(0){ rep(o){ e } }' ?
     */
    round(ctx1, repf(Field.fromSelfValue(0))(_ => repf(Field.fromSelfValue(0))(_ => expr1))) shouldEqual
      Export(
        / -> Field(Map(0 -> 1), 1),
        Rep(0) -> Field(Map(0 -> 1), 1),
        Rep(0) / Rep(0) -> Field(Map(0 -> 1), 1)
      )
    round(ctx1, repf(Field.fromSelfValue(0))(_ => repf(Field.fromSelfValue(0))(_ => expr2))) shouldEqual
      Export(
        / -> Field(Map(0 -> 8), 8),
        Rep(0) -> Field(Map(0 -> 8), 8),
        Rep(0) / Rep(0) -> Field(Map(0 -> 8), 8),
        Rep(0) / Rep(0) / Rep(0) -> Field(Map(0 -> 8), 8)
      )
    // TODO: CURRENTLY NOT WORKING
/*    round(ctx1, repf(Field.fromSelfValue(0))(_ => repf(Field.fromSelfValue(0))(_ => expr3))) shouldEqual
      Export(
        / -> Field(Map(0 -> 5), 5),
        Rep(0) -> Field(Map(0 -> 5), 5),
        Rep(0) / Rep(0) -> Field(Map(0 -> 5), 5),
        Rep(0) / Rep(0) / FoldHood(0) -> Field(Map(0 -> 5), 5),
        Rep(0) / Rep(0) / FoldHood(0) / Nbr(0) -> Field(Map(0 -> 5), 5)
      )*/

    /* Testing more NBRs within foldhood
     */
    // TODO: CURRENTLY NOT WORKING
/*    round(ctx1, foldhoodf(nbrf(sense[Int]("sensor")) + nbrf(sense[Int]("sensor")))((a, b) => a + b)) shouldEqual
      Export(
        / -> Field(Map(0 -> 10), 10),
        FoldHood(0) -> Field(Map(0 -> 10), 10),
        FoldHood(0) / Nbr(0) -> Field(Map(0 -> 5), 5),
        FoldHood(0) / Nbr(1) -> Field(Map(0 -> 5), 5)
      )*/
  }

  FOLDHOODF("should support aggregating information from aligned neighbors") {
    // ARRANGE
    val exp1 = Map(2 -> Export(/ -> "a", FoldHood(0) -> "a"), 4 -> Export(/ -> "b", FoldHood(0) -> "b"))
    val ctx1 = ctx(0, exp1)
    // ACT + ASSERT
    round(ctx1, foldhoodf(Field.fromSelfValue("a"))((x, _) => x + "z")).root[String]() shouldBe "azzz"

    // ARRANGE
    val exp2 = Map(2 -> Export(/ -> "a", FoldHood(0) -> "a"), 4 -> Export(/ -> "b", FoldHood(0) -> "b"))
    val ctx2 = ctx(selfId = 0, exports = exp2)
    // ACT + ASSERT (should failback to 'init' when neighbors lose alignment within foldhood)
    round(
      ctx2,
      foldhood(-5)(_ + _)(if (nbr(false)) {
        0
      }
      else {
        1
      })
    ).root[Int]() shouldBe -14
  }

/*  NBR("needs not to be nested into fold") {
    // ARRANGE
    val ctx1 = ctx(selfId = 0)
    // ACT + ASSERT
    round(ctx1, nbr(1)).root[Int]() shouldBe 1
  }*/

/*  NBR("should support interaction between aligned devices") {
    // ARRANGE
    val exp1 = Map(
      1 -> exportFrom(/ -> "any", FoldHood(0) -> 1, FoldHood(0) / Nbr(0) -> 1),
      2 -> exportFrom(/ -> "any", FoldHood(0) -> 2, FoldHood(0) / Nbr(0) -> 2)
    )
    val ctx1 = ctx(selfId = 0, exports = exp1)
    // ACT
    val res1 = round(ctx1, foldhood(0)(_ + _)(if (nbr(mid()) == mid()) 0 else 1))
    // ASSERT
    res1.root[Int]() shouldBe 2
    res1.get(FoldHood(0) / Nbr(0)) shouldBe Some(0)
  }*/

/*  REP("should support dynamic evolution of fields") {
    // ARRANGE
    val ctx1 = ctx(selfId = 0)
    // ACT
    val exp1 = round(ctx1, rep(9)(_ * 2))
    // ASSERT (use initial value)
    exp1.root[Int]() shouldBe 18
    exp1.get(/(Rep(0))) shouldBe Some(18)

    // ARRANGE
    val exp = Map(0 -> exportFrom(Rep(0) -> 7))
    val ctx2 = ctx(selfId = 0, exports = exp)
    // ACT
    val exp2 = round(ctx2, rep(9)(_ * 2))
    // ASSERT (build upon previous state)
    exp2.root[Int]() shouldBe 14
    exp2.get(/(Rep(0))) shouldBe Some(14)
  }*/

/*  BRANCH("should support domain restriction, thus affecting the structure of exports") {
    // ARRANGE
    def program =
      rep(0) { x => branch(x % 2 == 0)(7)(rep(4)(x => 4)); x + 1 }

    // ACT
    val exp = round(ctx(0), program)
    // ASSERT
    exp.root[Int]() shouldBe 1
    //exp.get(path(If(0, true), Rep(0))) shouldBe Some(7)
    //exp.get(path(If(0, false), Rep(0))) shouldBe None

    // ACT
    val ctx2 = ctx(0, Map(0 -> exportFrom(Rep(0) -> 1)))
    val exp2 = round(ctx2, program)

    exp2.root[Int]() shouldBe 2
    //exp2.get(path(If(0, true), Rep(0))) shouldBe None
    //exp2.get(path(If(0, false), Rep(0))) shouldBe Some(4)
    //exp2.get(path(Rep(0), If(0, false), Rep(0))) shouldBe Some(4)
  }*/

/*  SENSE("should simply evaluate to the last value read by sensor") {
    // ARRANGE
    val ctx1 = ctx(0, Map(), Map("a" -> 7, "b" -> "high"))
    // ACT + ASSERT (failure as no sensor 'c' is found)
    round(ctx1, sense[Any]("a")).root[Int]() shouldBe 7
    round(ctx1, sense[Any]("b")).root[String]() shouldBe "high"
  }*/

/*  SENSE("should fail if the sensor is not available") {
    // ARRANGE
    val ctx1 = ctx(0, Map(), Map("a" -> 1, "b" -> 2))
    // ACT + ASSERT (failure as no sensor 'c' is found)
    intercept[AnyRef](round(ctx1, sense[Any]("c")))
    // ACT + ASSERT (failure if an existing sensor does not provide desired kind of data)
    intercept[AnyRef](round(ctx1, sense[Boolean]("a")))
  }*/

/*  MID("should simply evaluate to the ID of the local device") {
    // ACT + ASSERT
    round(ctx(77), mid()).root[Int]() shouldBe 77
    round(ctx(8), mid()).root[Int]() shouldBe 8
  }*/

/*  NBRVAR("should work as a ''sensor'' for neighbors") {
    // ARRANGE
    val nbsens = Map("a" -> Map(0 -> 0, 1 -> 10, 2 -> 17), "b" -> Map(0 -> "x", 1 -> "y", 2 -> "z"))
    val ctx1 = ctx(0, Map(1 -> exportFrom(/ -> 10, FoldHood(0) -> 10)), Map(), nbsens)
    // ACT + ASSERT
    round(ctx1, foldhood(0)((a, b) => if (a > b) a else b)(nbrvar[Int]("a"))).root[Int]() shouldBe 10

    // ACT + ASSERT (should fail when used outside fooldhood
    intercept[Exception](round(ctx1, nbrvar[Int]("a")))
  }*/

/*  NBRVAR("should fail if the neighborhood ''sensor'' is not available") {
    // ARRANGE
    val nbsens = Map("a" -> Map(0 -> 0, 1 -> 10, 2 -> 17))
    val ctx1 = ctx(0, Map(1 -> exportFrom(/ -> 10)), Map(), nbsens)
    // ACT + ASSERT (failure because of bad type)
    intercept[AnyRef](round(ctx1, foldhood("")(_ + _)(nbrvar[String]("a"))))
    // ACT + ASSERT (failure because not found)
    intercept[AnyRef](round(ctx1, foldhood(0)(_ + _)(nbrvar[Int]("xxx"))))
  }*/

/*  BUILTIN("minHood and minHood+, maxHood and maxHood+") {
    // ARRANGE
    val exp1 = Map(
      1 -> exportFrom(/ -> "any", FoldHood(0) -> 10, FoldHood(0) / Nbr(0) -> 10),
      2 -> exportFrom(/ -> "any", FoldHood(0) -> 5, FoldHood(0) / Nbr(0) -> 5)
    )
    val ctx1 = ctx(0, exp1, Map("sensor" -> 3, "sensor2" -> 20))
    // ACT + ASSERT
    round(ctx1, minHood(nbr(sense[Int]("sensor")))).root[Int] shouldBe 3
    round(ctx1, maxHood(nbr(sense[Int]("sensor2")))).root[Int] shouldBe 20

    /** N.B. foldHoodPlus, minHoodPlus, maxHoodPlus should be considered as
     * *  "library" methods (not primitives), thus it may be better to not
     * *  test exports for them. For now, however, we keep these tests.
     * */
    // ARRANGE
    val exp2 = Map(
      1 -> exportFrom(/ -> "any", FoldHood(0) -> 1, FoldHood(0) / Nbr(0) -> 1, FoldHood(0) / Nbr(1) -> 10),
      2 -> exportFrom(/ -> "any", FoldHood(0) -> 2, FoldHood(0) / Nbr(0) -> 2, FoldHood(0) / Nbr(1) -> 5)
    )
    // Note: the export on Nbr(0) is for the internal call to nbr(mid())
    val ctx2 = ctx(0, exp2, Map("sensor" -> 3, "sensor2" -> 20))
    // ACT + ASSERT
    round(ctx2, minHoodPlus(nbr(sense[Int]("sensor")))).root[Int] shouldBe 5
    round(ctx2, maxHoodPlus(nbr(sense[Int]("sensor2")))).root[Int] shouldBe 10
  }*/

/*  Nesting("REP into FOLDHOOD should be supported") {
    // ARRANGE
    val ctx1 = ctx(0, Map(1 -> exportFrom(FoldHood(0) -> 7), 2 -> exportFrom(FoldHood(0) -> 7)))

    def program1 = foldhood("init")(_ + _)(rep(0)(_ + 1) + "")

    val ctx2 =
      ctx(0, Map(1 -> exportFrom(FoldHood(0) -> 7), 2 -> exportFrom(FoldHood(0) -> 7, FoldHood(0) / Nbr(0) -> 7)))

    def program2 = foldhood("init")(_ + _)(nbr(rep(0)(_ + 1)) + "")

    // ACT + ASSERT
    val exp1 = round(ctx1, program1)
    exp1.root[String] shouldEqual "init111"
    ctx1.updateExport(0, exp1)
    round(ctx1, program1).root[String] shouldEqual "init222"

    val exp2 = round(ctx2, program2)
    assertPossibleFolds("init", List("init", "7", "1")) {
      exp2.root[String]
    }
    ctx2.updateExport(0, exp2)
    assertPossibleFolds("init", List("init", "7", "2")) {
      round(ctx2, program2).root[String]
    }
  }*/

/*  Nesting("FOLDHOOD into FOLDHOOD should be supported") {
    // ARRANGE
    val ctx1 = ctx(
      0,
      Map(
        1 -> exportFrom(FoldHood(0) -> 7, FoldHood(0) / FoldHood(0) -> 7),
        2 -> exportFrom(
          FoldHood(0) -> 7,
          FoldHood(0) / Nbr(0) -> 7,
          FoldHood(0) / FoldHood(0) -> 7,
          FoldHood(0) / Nbr(0) / FoldHood(0) -> 7
        )
      )
    )

    // ACT + ASSERT
    round(ctx1, foldhood("init")(_ + _)(foldhood(0)(_ + _)(1) + "")).root[String] shouldEqual "init333"

    assertPossibleFolds("init", List("init", "7", "2")) {
      round(ctx1, foldhood("init")(_ + _)(nbr(foldhood(0)(_ + _)(1)) + "")).root[String]
    }
  }*/

/*  DeferredExports("should work in a recurrent fashion") {
    val exp = round(
      ctx(0), {
        vm.newExportStack
        rep(0) { _ =>
          vm.newExportStack
          rep(0) { _ =>
            vm.newExportStack
            rep(0)(_ + 1)
            vm.mergeExport
            1
          }
          foldhood(0)(_ + _)(nbr(1))
          vm.discardExport
          1
        }
        foldhood(0)(_ + _)(nbr(1))
        vm.mergeExport
      }
    )

    exp.get(/ / Rep(0)) should be(defined)
    exp.get(/ / FoldHood(1)) should be(defined)
    exp.get(/ / Rep(0) / Rep(0)) should not be defined
    exp.get(/ / Rep(0) / FoldHood(1)) should not be defined
    exp.get(/ / Rep(0) / Rep(0) / Rep(0)) should not be defined
  }*/

/*  private def assertPossibleFolds(init: Any, a: List[Any])(expr: => Any): Unit =
    a.permutations.map(l => init + l.mkString).toList should contain(expr)*/
