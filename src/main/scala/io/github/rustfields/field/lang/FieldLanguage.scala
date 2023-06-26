package io.github.rustfields.field.lang

import io.github.rustfields.field.{FieldOps, Fields}
import io.github.rustfields.lang.FieldCalculusSyntax
import io.github.rustfields.vm.Slot.Nbr

trait FieldLanguage:
  type F[A]
  def repf[A](init: => F[A])(fun: F[A] => F[A]): F[A]
  def nbrf[A](expr: => F[A]): F[A]
  def branchf[A](cond: F[Boolean])(thn: => F[A])(els: => F[A]): F[A]

trait FieldLanguageImpl extends FieldLanguage with Fields with FieldOps:
  self: FieldCalculusSyntax =>

  override type F[A] = Field[A]

  override def branchf[A](cond: Field[Boolean])(thn: => Field[A])(els: => Field[A]): Field[A] =
    branch(cond)(thn)(els)

  override def nbrf[A](expr: => Field[A]): Field[A] =
    vm.nest(Nbr(vm.index))(write = vm.unlessFoldingOnOthers) {
      val nbrs = vm.alignedNeighbours()
      Field(expr.getMap.filter((nbrId, _) => {
        nbrs.contains(nbrId)
      }),
        expr.default)
    }

  override def repf[A](init: => Field[A])(fun: Field[A] => Field[A]): Field[A] =
    rep(init)(fun)