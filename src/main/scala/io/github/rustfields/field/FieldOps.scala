package io.github.rustfields.field


import cats.kernel.Monoid
import cats.syntax.all.*

import scala.annotation.targetName

/**
 * This trait defines additional syntax and operations with [[io.github.rustfields.field.Fields]]
 */
trait FieldOps:
  self: Fields =>

  export FieldGivens.given
  export cats.instances.all._

  extension[A](fld: Field[A])
    @targetName("mappendWith")
    def |++|(other: Field[A])(using m: Monoid[A]): Field[A] = mappendFields(fld, other)

  /**
   * Apply a field of functions to a field
   * @param f1 the field to apply the function field to
   * @param f2 the field of functions to apply
   * @tparam A the type of the argument of the function field
   * @tparam B the type of the result of the function field
   * @return a field which is the result of applying the functions from the function field to the values of the first field
   */
  def applyFunctionField[A, B](f1: Field[A], f2: Field[A => B]): Field[B] =
    for
      v1 <- f1
      v2 <- f2
    yield v2(v1)

  /**
   * Combines two fields of monoids using the monoid's combine method
   * @param f1 the first field
   * @param f2 the second field
   * @tparam A the type of the fields
   * @return a field which is the result of combining the values of the two fields using the monoid's combine method
   */
  def mappendFields[A: Monoid](f1: Field[A], f2: Field[A]): Field[A] =
    combineFields(f1, f2)(_ |+| _)

  /**
   * Combine two fields using a provided function
   * @param f1 the first field
   * @param f2 the second field
   * @param func the function to combine the values of the fields
   * @tparam A the type of the first field
   * @tparam B the type of the second field
   * @tparam C the type of the result of the function
   * @return a field which is the result of combining the values of the two fields using the provided function
   */
  def combineFields[A, B, C](f1: Field[A], f2: Field[B])(
    func: (A, B) => C
  ): Field[C] =
    for
      v1 <- f1
      v2 <- f2
    yield func(v1, v2)

