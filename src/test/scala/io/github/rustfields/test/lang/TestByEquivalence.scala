package io.github.rustfields.test.lang

import io.github.rustfields.field.lang.FieldLib
import io.github.rustfields.field.syntax.FieldSyntax
import io.github.rustfields.test.FieldTest
import io.github.rustfields.field.DefaultableInstances.given
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import cats.instances.all.*
import cats.syntax.all.*
import scala.language.implicitConversions
import scala.collection.mutable
import scala.util.Random


class TestByEquivalence extends AnyFlatSpec with FieldTest with FieldLib with FieldSyntax with Matchers:
  given node: TestByEquivalence = this

  class Fixture:
    val random = new Random(0)
    val execSequence: Seq[Int] = LazyList.continually(random.nextInt(3)).take(100)
    val devicesAndNbrs: Map[Int, List[Int]] = fullyConnectedTopologyMap(mutable.Iterable(0, 1, 2))

  "fold" should "work with multiple nbrs" in {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf[Int](_ + _) {
        nbrf(1) |++| nbrf(2) |++| nbrf(mid())
      }
    } {
      foldhoodf[Int](_ + _) {
        nbrf(1 + 2 + mid())
      }
    }
  }

  "nbr.nbr" should "be ignored" in {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf[Int](_ + _) {
        nbrf(mid() + nbrf(mid()).selfValue)
      }
    } {
      2 * foldhoodf[Int](_ + _) {
        nbrf(mid())
      }
    }
  }

  "rep.nbr" should "be ignored on first argument" in {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence){
      foldhoodf[Int](_ + _) {
        repf(nbrf(mid()))(old => old)
      }
    }{
      foldhoodf[Int](_ + _) {
        repf(mid())(old => old)
      }
    }
  }

  "rep.nbr" should "be ignored overall" in {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf[Int](_ + _) {
        repf(nbrf(mid())) { old =>
          old |++| nbrf(old) |++| nbrf(mid())
        }
      }
    } {
      foldhoodf[Int](_ + _)(1) *
        repf[Int](mid()) { old =>
          old |++| old |++| mid()
        }
    }
  }

  "fold.init nbr" should "be ignored" in {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf[Int](_ + _) {
        fromExpression(0) {
          foldhoodf[Int](_ + _) {
            fromExpression(0) {
              nbrf(mid())
            }
          }
        }
      }
    } {
      foldhoodf[Int](_ + _) {
        fromExpression(0) {
          foldhoodf[Int] (_ + _) {
            fromExpression(0) {
              mid()
            }
          }
        }
      }
    }
  }

  "fold.fold" should "work" in  {
    val fixture = new Fixture

    assertEquivalence(fixture.devicesAndNbrs, fixture.execSequence) {
      foldhoodf[Double](_ + _) {
        fromExpression(0.0) {
          foldhoodf[Double](_ + _) {
            fromExpression(0.0) {0.0}
          }
        }
      }
    } {
      Math.pow(foldhoodf[Double](_ + _)(fromExpression(0.0) {0.0}), 2)
    }
  }