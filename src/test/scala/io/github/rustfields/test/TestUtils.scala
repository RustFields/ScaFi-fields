package io.github.rustfields.test

import io.github.rustfields.lang.FieldCalculusInterpreter
import io.github.rustfields.vm.{Context, Export, Sensor}

import scala.collection.mutable

trait TestUtils:
    
  def ctx(
           selfId: Int,
           exports: Map[Int, Export] = Map(),
           lsens: Map[String, Any] = Map(),
           nbsens: Map[String, Map[Int, Any]] = Map()
         ): Context = {
    val localSensorsWithId = lsens.map { case (k, v) => (Sensor(k): Sensor) -> v }
    val neighborhoodSensorWithId = nbsens.map { case (k, v) =>
      (Sensor(k): Sensor) -> v
    }
    Context(selfId, exports, localSensorsWithId, neighborhoodSensorWithId)
  }

  def assertEquivalence[A](
                            nbrs: Map[Int, List[Int]],
                            execOrder: Iterable[Int],
                            comparer: (A, A) => Boolean = (_: Any) == (_: Any)
                          )(program1: => Any)(program2: => Any)(using interpreter: FieldCalculusInterpreter): Boolean = {
    val states = mutable.Map[Int, (Export, Export)]()
    execOrder.foreach { curr =>
      val nbrExports = states.view.filterKeys(nbrs(curr).contains(_))
      val currCtx1 = ctx(curr, exports = nbrExports.mapValues(_._1).toMap)
      val currCtx2 = ctx(curr, exports = nbrExports.mapValues(_._2).toMap)

      val exp1 = interpreter.round(currCtx1, program1)
      val exp2 = interpreter.round(currCtx2, program2)
      if (!comparer(exp1.root(), exp2.root())) {
        throw new Exception(s"Not equivalent: \n$exp1\n$currCtx1\n--------\n$exp2\n$currCtx2")
      }
      states.put(curr, (exp1, exp2))
    }
    true
  }

  def compareLocally[A](prog1: => Any)(prog2: => Any)(comparer: (A, A) => Boolean = (_: Any) == (_: Any))(using interpreter: FieldCalculusInterpreter): Boolean =
    val exp1 = interpreter.round(ctx(0), prog1)
    val exp2 = interpreter.round(ctx(0), prog2)
    if comparer(exp1.root(), exp2.root()) then 
      true 
    else
      throw new Exception(s"Not equivalent: \n${prog1}\n${prog2}")