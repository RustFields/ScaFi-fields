package io.github.rustfields.field.syntax

import io.github.rustfields.field.Fields

trait FieldSyntax:
  self: Fields =>
  
  extension[A](fld: Field[A])
    def neighbouring: Field[A] = Field.toNeighbouring(fld)
    def get(id: Int): A = Field.get(fld, id)
    def selfValue: A = Field.selfValue(fld)
    def fold(z: A)(aggr: (A, A) => A): A = Field.fold(fld)(z)(aggr)