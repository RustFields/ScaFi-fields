package io.github.rustfields.test.lang

import io.github.rustfields.test.{FieldTest, TestUtils}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class TestByEquivalence extends AnyFunSpec with FieldTest with Matchers:
  val checkThat = new ItWord

  given node: TestByEquivalence = this

  class Fixture:
    val random = new Random(0)
    val execSequence: Seq[Int] = LazyList.continually(random.nextInt(3)).take(100)
    val devicesAndNbrs: Map[Int, List[Int]] = fullyConnectedTopologyMap(mutable.Iterable(0, 1, 2))

  checkThat("multiple nbrs in fold work well"){
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhood(0)(_ + _)(nbr(1) + nbr(2) + nbr(mid()))
    } {
      foldhood(0)(_ + _)(nbr(1 + 2 + mid()))
    }
  }

  checkThat("nbr.nbr is to be ignored") {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhood(0)(_ + _)(nbr(mid() + nbr(mid())))
    } {
      2 * foldhood(0)(_ + _)(nbr(mid()))
    }
  }

  checkThat("rep.nbr is to be ignored on first argument") {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence){
      foldhood(0)(_ + _) {
        rep(nbr(mid()))(old => old)
      }
    } {
      foldhood(0)(_ + _) {
        rep(mid())(old => old)
      }
    }
  }

  //todo does not pass
  checkThat("rep.nbr is to be ignored overall") {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhood(0)(_ + _) {
        rep(nbr(mid())) { old =>
          old + nbr(old) + nbr(mid())
        }
      }
    } {
      foldhood(0)(_ + _)(1) *
        rep(mid()) { old =>
          old * 2 + mid()
        }
    }
  }

  checkThat("fold.fold basically works") {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhood(0)(_ + _) {
        foldhood(0)(_ + _)(1)
      }
    } {
      Math.pow(foldhood(0)(_ + _)(1), 2)
    }
  }

  checkThat("performance does not degrade when nesting foldhoods") {
    val execSequence = LazyList.continually(Random.nextInt(100)).take(1000)
    val devicesAndNbrs = fullyConnectedTopologyMap(mutable.ArraySeq.from((0 to 99)))
    val max = 0.000001

    //fold.fold : performance
    //Note: attention to double overflow
    assertEquivalence(devicesAndNbrs,
      execSequence,
      (x: Double, y: Double) => Math.abs(x - y ) / Math.max(Math.abs(x), Math.abs(y)) < max
    ) {
      foldhood(0.0)(_ + _) {
        foldhood(0.0)(_ + _) {
          foldhood(0.0)(_ + _) {
            foldhood(0.0)(_ + _) {
              foldhood(0.0)(_ + _) {
                foldhood(0.0)(_ + _) {
                  foldhood(0.0)(_ + _) {
                    foldhood(0.0)(_ + _) {
                      foldhood(0.0)(_ + _) {
                        foldhood(0.0)(_ + _)(1.0)
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    } {
      Math.pow(foldhood(0.0)(_ + _)(1.0), 10)
    }
  }