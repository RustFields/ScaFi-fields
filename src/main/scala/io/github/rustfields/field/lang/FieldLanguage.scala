package io.github.rustfields.field.lang

import io.github.rustfields.field.syntax.FieldSyntax
import io.github.rustfields.field.{FieldOps, Fields}
import io.github.rustfields.lang.{AggregateProgram, FieldCalculusInterpreter, FieldCalculusSyntax}
import io.github.rustfields.vm.Slot.{FoldHood, Nbr, Rep}

trait FieldLanguage:
  type F[A]
  def repf[A](init: => F[A])(fun: F[A] => F[A]): F[A]
  def nbrf[A](expr: => A): F[A]
  def branchf[A](cond: F[Boolean])(thn: => F[A])(els: => F[A]): F[A]
  def foldhoodf[A](fun: (A, A) => A)(init: => F[A]): A
  def fromExpression[A](default: => A)(expr: => A): F[A]

trait FieldLanguageImpl extends FieldLanguage with Fields with FieldOps with FieldSyntax:
  self: FieldCalculusSyntax =>

  override type F[A] = Field[A]

  override def branchf[A](cond: Field[Boolean])(thn: => Field[A])(els: => Field[A]): Field[A] =
    branch(cond)(thn)(els)

  override def nbrf[A](expr: => A): Field[A] =
    def readNeighbourValue(id: Int): Option[A] =
      vm.context.readExportValue[A](id, vm.status.path) match
        case Some(value: Field[A]) => value.getMap.get(id)
        case Some(value: A) => Some(value)
        case _ => None

    vm.nest(Nbr(vm.index))(true) {
      val nbrs = vm.alignedNeighbours()
      Field(nbrs.map(nbrId => nbrId -> readNeighbourValue(nbrId).getOrElse(expr)).toMap, expr)
    }

  override def repf[A](init: => Field[A])(fun: Field[A] => Field[A]): Field[A] =
    vm.nest(Rep(vm.index))(write = vm.unlessFoldingOnOthers) {
      // get the old value of the field
      val oldField = vm.context.readExportValue(vm.self, vm.status.path).getOrElse(init)
      fun(oldField)
    }

  override def foldhoodf[A](fun: (A, A) => A)(init: => Field[A]): A =
    vm.nest(FoldHood(vm.index))(write = true) {
        vm.isolate(init.neighbouring.fold(vm.locally(init.default))(fun))
    }
    
  override def fromExpression[A](default: => A)(expr: => A): Field[A] =
    Field.fromExpression(default)(expr)

object FieldLanguageImpl:
  type FieldLanguageComponent = FieldLanguageImpl with Fields with FieldSyntax with FieldCalculusSyntax

trait FieldLanguageInterpreter extends FieldCalculusInterpreter with FieldLanguageImpl

trait FieldLanguageProgram extends AggregateProgram with FieldLanguageImpl