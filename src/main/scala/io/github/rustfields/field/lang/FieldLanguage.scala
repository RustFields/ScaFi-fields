package io.github.rustfields.field.lang

import io.github.rustfields.field.{FieldOps, Fields}
import io.github.rustfields.lang.FieldCalculusSyntax
import io.github.rustfields.vm.Slot.{Nbr, Rep}

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
    def readNeighbourValue[T](id: Int): Option[T] = vm.context.readExportValue(id, vm.status.path)

    vm.nest(Nbr(vm.index))(write = vm.unlessFoldingOnOthers) {
      val nbrs = vm.alignedNeighbours()
      Field(
        nbrs.map(nbrId => { // for each neighbour
          nbrId -> readNeighbourValue[Field[A]](nbrId) // get the value sent to the current device (`self`)
            .getOrElse(expr).getMap // otherwise, provide the default
            .getOrElse(vm.self, // if the neighbour has no value for the current device (`self`)
              readNeighbourValue[Field[A]](nbrId).getOrElse(expr).default // provide the default
            )
        }).toMap,
        expr.default)
    }

  override def repf[A](init: => Field[A])(fun: Field[A] => Field[A]): Field[A] =
    vm.nest(Rep(vm.index))(write = vm.onlyWhenFoldingOnSelf) {
      // get the old value of the field
      val oldField = vm.context.readExportValue(vm.self, vm.status.path).getOrElse(init)
      fun(oldField)
    }