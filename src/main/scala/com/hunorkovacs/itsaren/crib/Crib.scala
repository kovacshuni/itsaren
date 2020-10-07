package com.hunorkovacs.itsaren.crib

import zio.Task
import zio.json._
import org.http4s.EntityEncoder

case class Crib(id: String, address: String, phone: String)

object Crib {
  implicit val cribEncoder: JsonEncoder[Crib] = DeriveJsonEncoder.gen[Crib]
  implicit val cribDecoder: JsonDecoder[Crib] = DeriveJsonDecoder.gen[Crib]

  implicit val cribEntityEncoder: EntityEncoder[Task, Crib] =
    EntityEncoder.stringEncoder[Task].contramap(c => c.toJson)
}
