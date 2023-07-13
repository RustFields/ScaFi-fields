package io.github.rustfields.test

import io.github.rustfields.field.lang.{FieldLanguageInterpreter, FieldLib}

trait FieldTest extends FieldLanguageInterpreter with FieldLib with TestUtils
