package io.github.rustfields.test

import cats.Monad
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