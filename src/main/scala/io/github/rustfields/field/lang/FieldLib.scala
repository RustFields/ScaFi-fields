package io.github.rustfields.field.lang

trait FieldLib:
  self: FieldLanguageImpl.FieldLanguageComponent =>

  /**
   * Creates a field "on the fly" from an expression and then performs a folded operation on it 
   * @param aggr the aggregator function to fold the field with
   * @param default a default value for the field creation
   * @param expr the expression from which to create the field
   * @tparam A the type of the field
   * @return the result of the folded operation
   */
  def foldFrom[A](aggr: (A, A) => A)(default: =>A)(expr: =>A): A =
    foldhoodf(aggr) {
      fromExpression(default)(expr)
    }
