package io.github.rustfields.field

import cats.Monad
import io.github.rustfields.lang.FieldCalculusSyntax
import scala.language.implicitConversions

trait Fields:
  self: FieldCalculusSyntax =>

  /** A field is a map from device ids to values of type A. When a device is not
   * in the map, the default value is used.
   *
   * @tparam A
   * the type of the field
   */
  case class Field[A](getMap: Map[Int, A], override val default: A)
    extends Defaultable[A]

  object Field:
    def apply[A: Defaultable](m: Map[Int, A]): Field[A] =
      val d = summon[Defaultable[A]].default
      Field(m, d)

    /** Lifts a value to a field of values
     *
     * @param a
     * the value to lift
     * @tparam A
     * the type of the value
     * @return
     * the field with an empty map and the lifted value as a default
     */
    def lift[A](a: A): Field[A] = Field(Map.empty, a)

    /**
     * Create a field with a single value for the current device, 
     * using said value as the default value.
     * @param a the value to use
     * @tparam A the type of the value
     * @return the field
     */
    def fromSelfValue[A](a: A): Field[A] = 
      Field(Map(mid() -> a), a)

    /**
     * Dynamically creates a Field from a provided expression.
     * Where the expression is not evaluated for a neighbour, the default value is used.
     * @param expr the expression to evaluate for each neighbour
     * @param default the default value to use when the expression is not evaluated for a neighbour
     * @tparam A the type of the field
     * @return A neighbouring field with the expression value of each neighbour that evaluated that,
     *         or the default value
     */
    def fromExpression[A](default: A)(expr: => A): Field[A] =
      val nbrs = vm.alignedNeighbours()
      Field(nbrs.map(id => id -> vm.foldedEval(expr)(id).getOrElse(default)).toMap, default)

    /**
     * Returns the value of the field for the given device
     * @param f the field
     * @param id the device id
     * @tparam A the type of the field
     * @return the value of the field for the given device
     */
    def get[A](f: Field[A], id: Int): A =
      f.getMap.getOrElse(id, f.default)

    /**
     * Returns the value of the field for the current device
     * @param f the field
     * @tparam A the type of the field
     * @return the value of the field for the current device
     */
    def selfValue[A](f: Field[A]): A =
      get(f, mid())

    /**
     * Returns a neighbouring field created from the given field.
     * @param f the field
     * @tparam A the type of the field
     * @return A neighbouring field with the aligned neighbours of the current device that are present in the given field.
     */
    def toNeighbouring[A](f: Field[A]): Field[A] =
      val nbrs = vm.alignedNeighbours()
      Field[A](f.getMap.filter((id, _) => nbrs.contains(id)), f.default)

    /**
     * Folds the elements of the field using the given aggregation function. The traversal order is not specified.
     * @param f the field
     * @param z the initial value
     * @param aggr the aggregation function
     * @tparam A the type of the field
     * @return the result of applying the fold operator op between all the elements and z, or z if this collection is empty.
     */
    def fold[A](f: Field[A])(z: A)(aggr: (A, A) => A): A =
      f.getMap.values.fold(z)(aggr)
    
  object FieldGivens:
    given localToFieldConversion[A]: Conversion[A, Field[A]] with
      def apply(a: A): Field[A] = Field.fromSelfValue(a)
      
    given fieldToLocalConversion[A]: Conversion[Field[A], A] with
      def apply(field: Field[A]): A = Field.selfValue(field)

    given Monad[Field] with
      override def pure[A](x: A): Field[A] = Field.lift(x)

      override def flatMap[A, B](fa: Field[A])(f: A => Field[B]): Field[B] =
        Field(
          fa.getMap.map { case (id, a) =>
            id -> {
              val b = f(a)
              b.getMap.getOrElse(id, b.default)
            }
          },
          f(fa.default)
        )

      override def tailRecM[A, B](a: A)(
        func: A => Field[Either[A, B]]
      ): Field[B] =
        val mapList = func(a).getMap.toList

        val newMap = mapList.foldLeft(Map.empty[Int, B]) {
          case (acc, (deviceId, either)) =>
            either match
              case Left(a1) =>
                val field = tailRecM(a1)(func)
                acc.updated(
                  deviceId,
                  field.getMap.getOrElse(deviceId, field.default)
                )
              case Right(b) => acc.updated(deviceId, b)
        }

        val default = func(a).default match
          case Left(a1) => tailRecM(a1)(func).default
          case Right(b) => b

        Field(newMap, default)