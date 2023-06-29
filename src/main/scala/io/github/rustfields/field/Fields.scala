package io.github.rustfields.field

import cats.{Monad, catsInstancesForId}
import io.github.rustfields.lang.FieldCalculusSyntax

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
     * Returns the value of the field for the current device
     * @param f the field
     * @tparam A the type of the field
     * @return the value of the field for the current device
     */
    def selfValue[A](f: Field[A]): A =
      f.getMap.getOrElse(mid(), f.default)
  
  object FieldGivens:
    given localToFieldConversion[A]: Conversion[A, Field[A]] with
      def apply(a: A): Field[A] = Field.lift(a)
      
    given fieldToLocalConversion[A]: Conversion[Field[A], A] with
      def apply(field: Field[A]): A = field.getMap.getOrElse(mid(), field.default)

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
          f(fa.default).default
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