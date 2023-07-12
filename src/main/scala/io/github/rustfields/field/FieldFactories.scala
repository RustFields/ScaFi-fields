package io.github.rustfields.field

trait FieldFactories:
  self: Fields =>
  
  export DefaultableInstances.given
  def fromDefaultableExpr[A](expr: => A)(using d: Defaultable[A]): Field[A] =
    Field.fromExpression(d.default)(expr)
  def fromIntExpr(expr: => Int): Field[Int] =
    fromDefaultableExpr[Int](expr)

  def fromDouble(expr: => Double): Field[Double] =
    fromDefaultableExpr[Double](expr)
    
  def fromString(expr: => String): Field[String] =
    fromDefaultableExpr[String](expr)
    
  def fromBoolean(expr: => Boolean): Field[Boolean] =
    fromDefaultableExpr[Boolean](expr)