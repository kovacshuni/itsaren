package com.hunorkovacs.itsaren.crib

import zio.Task
import org.http4s.EntityDecoder
import org.http4s.circe._
import zio.interop.catz._
import io.circe._
import io.circe.generic.semiauto._

case class CribNoId(address: String, phone: String)

object CribNoId {
  implicit val cribNoIdEncoder: Encoder[CribNoId] = deriveEncoder[CribNoId]
  implicit val cribNoIdDecoder: Decoder[CribNoId] = deriveDecoder[CribNoId]

  implicit val ee: EntityDecoder[Task, CribNoId] = jsonOf[Task, CribNoId]
}
