package io.github.rustfields.test

import cats.instances.all._
import cats.kernel.Monoid
import cats.syntax.all._
import io.github.rustfields.test.Fields

trait FieldOps:
  self: Fields =>
  
  def applyToAll[A, B](f: Field[A], func: A => B): Field[B] =
    applyFunctionField(f, Field.lift(func))

  def applyFunctionField[A, B](f1: Field[A], f2: Field[A => B]): Field[B] =
    for
      v1 <- f1
      v2 <- f2
    yield v2(v1)

  def mappendFields[A: Monoid](f1: Field[A], f2: Field[A]): Field[A] =
    combineFields(f1, f2)(_ |+| _)

  def combineFields[A, B, C](f1: Field[A], f2: Field[B])(
    func: (A, B) => C
  ): Field[C] =
    for
      v1 <- f1
      v2 <- f2
    yield func(v1, v2)

