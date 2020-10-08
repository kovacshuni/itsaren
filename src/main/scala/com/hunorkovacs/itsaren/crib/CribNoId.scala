package com.hunorkovacs.itsaren.crib

import io.circe._
import io.circe.generic.semiauto._

case class CribNoId(address: String, phone: String)

object CribNoId {
  implicit val cribNoIdEncoder: Encoder[CribNoId] = deriveEncoder[CribNoId]
  implicit val cribNoIdDecoder: Decoder[CribNoId] = deriveDecoder[CribNoId]
}
