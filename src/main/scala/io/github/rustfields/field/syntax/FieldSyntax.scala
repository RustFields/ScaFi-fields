package io.github.rustfields.field.syntax

import io.github.rustfields.field.Fields

trait FieldSyntax:
  self: Fields =>
  
  import Field.*
  
  extension[A](fld: Field[A])
    def sV: A = selfValue(fld)
