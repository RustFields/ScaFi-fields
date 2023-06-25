package io.github.rustfields.test

import io.github.rustfields.lang.{FieldCalculusInterpreter, FieldCalculusSyntax}


trait FieldTest extends FieldCalculusInterpreter with Fields with FieldOps with MonadOps
