package io.github.rustfields.field

import cats.Monad
import cats.implicits.*
import scala.annotation.targetName

trait MonadOps:
  extension[F[_] : Monad, A] (fa: F[A])
    @targetName("mapWith")
    def >>[B](f: A => B): F[B] =
      fa.map(f)

    @targetName("flatMapWith")
    def >>=[B](f: A => F[B]): F[B] =
      fa.flatMap(f)

