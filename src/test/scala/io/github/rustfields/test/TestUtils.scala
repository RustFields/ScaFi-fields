package io.github.rustfields.test

import io.github.rustfields.lang.FieldCalculusInterpreter
import io.github.rustfields.vm.{Context, Export, Sensor}

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
