package io.github.rustfields.field.syntax

import io.github.rustfields.field.Fields

import scala.annotation.tailrec

trait FieldSyntax:
  self: Fields =>
  
  extension[A](fld: Field[A])
    def selfValue: A = Field.selfValue(fld)
    def fold(aggr: (A, A) => A): A = Field.fold(fld)(aggr)