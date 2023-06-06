package field

import cats.Monad
import cats.implicits.*
import scala.annotation.targetName

trait MonadicFields extends Fields:
  extension[F[_] : Monad, A] (fa: F[A])
    @targetName("mapWith")
    def >>[B](f: A => B): F[B] =
      fa.map(f)

    @targetName("flatMapWith")
    def >>=[B](f: A => F[B]): F[B] =
      fa.flatMap(f)

  /** Field Monad instance
   */
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

