package io.github.rustfields.test.lang

import io.github.rustfields.test.{FieldTest, TestUtils}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import io.github.rustfields.field.DefaultableInstances.given_Defaultable_Int
import org.scalatest.flatspec.AnyFlatSpec

import scala.collection.SeqView.Id
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class TestByEquivalence extends AnyFlatSpec with FieldTest with Matchers:
//  val checkThat = new ItWord

  given node: TestByEquivalence = this

  class Fixture:
    val random = new Random(0)
    val execSequence: Seq[Int] = LazyList.continually(random.nextInt(3)).take(100)
    val devicesAndNbrs: Map[Int, List[Int]] = fullyConnectedTopologyMap(mutable.Iterable(0, 1, 2))

  "fold" should "work with multiple nbrs" in {
    val fixture = new Fixture
    val f1: () => Field[Int] = () => nbrf(1) + nbrf(2) + nbrf(mid())
    val f2: () => Field[Int] = () => nbrf(1 + 2 + mid())

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf(f1())(_ + _)
    } {
      foldhoodf(f2())(_ + _)
    }
  }

  //todo check this
  /**
   * In the original scafi-core the test is:
   * assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
   *     foldhood(0)(_ + _)(nbr(mid() + nbr(mid())))
   *  } {
   *     2 * foldhood(0)(_ + _)(nbr(mid()))
   *  }
   */
  "nbr.nbr" should "be ignored" in {
    val fixture = new Fixture
    val f1: () => Field[Int] = () => nbrf(mid() + nbrf(mid()))
    val f2: () => Field[Int] = () => nbrf(mid())

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf(f1())(_ + _)
    } {
//      foldhoodf(2 * f2())(_ + _) //equivalent
      2 * foldhoodf(f2())(_ + _) //not equivalent but is made as the original in scafi-core (see above)
    }
  }

  "rep.nbr" should "be ignored on first argument" in {
    val fixture = new Fixture
    val f1: () => Field[Int] = () => repf(nbrf(mid()))(old => old)
    val f2: () => Field[Int] = () => repf(mid())(old => old)

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence){
      foldhoodf(f1())(_ + _)
    }{
      foldhoodf(f2())(_ + _)
    }
  }

  "rep.nbr" should "be ignored overall" in {
    val fixture = new Fixture
    val f1: () => Field[Int] = () => repf(nbrf(mid())) { old =>
      old + nbrf(old) + nbrf(mid())
    }
    val f2: () => Field[Int] = () => (1) *
      repf(mid()) { old =>
        old * 2 + mid()
      }

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf(f1())(_ + _)
    } {
      foldhoodf(f2())(_ + _)
    }
  }

  "fold.init nbr" should "be ignored" in {
    val fixture = new Fixture

    val f1: () => Field[Int] = () => foldhoodf(nbrf(mid()))(_ + _)
    val f2: () => Field[Int] = () => foldhoodf(mid())(_ + _)

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf(f1())(_ + _)
    } {
      foldhoodf(f2())(_ + _)
    }
  }

  //todo does not pass
  // > not equivalent
  "fold.fold" should "work" in  {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf(foldhoodf(0.0)(_ + _))(_ + _)
    } {
      Math.pow(foldhoodf(0.0)(_ + _), 2)
    }
  }

  //todo does not pass
  // > class io.github.rustfields.field.Fields$Field cannot be cast to class java.lang.Double
  "Performance" should "not degrade when nesting foldhoods" in {
    val execSequence = LazyList.continually(Random.nextInt(100)).take(1000)
    val devicesAndNbrs = fullyConnectedTopologyMap(mutable.ArraySeq.from((0 to 99)))
    val max = 0.000001

    //fold.fold : performance
    //Note: attention to double overflow
    assertEquivalence(devicesAndNbrs,
      execSequence,
      (x: Double, y: Double) => Math.abs(x - y ) / Math.max(Math.abs(x), Math.abs(y)) < max
    ) {
      foldhoodf(
        foldhoodf(
          foldhoodf(
            foldhoodf(
              foldhoodf(
                foldhoodf(
                  foldhoodf(
                    foldhoodf(
                      foldhoodf(
                        foldhoodf(0.0)(_ + _)
                      )(_ + _)
                    )(_ + _)
                  )(_ + _)
                )(_ + _)
              )(_ + _)
            )(_ + _)
          )(_ + _)
        )(_ + _)
      )(_ + _)
    } {
      Math.pow(foldhoodf(0.0)(_ + _), 10)
    }
  }