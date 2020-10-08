package com.hunorkovacs.itsaren.crib

import io.circe._
import io.circe.generic.semiauto._

case class Crib(id: String, address: String, phone: String)

object Crib {
  implicit val cribEncoder: Encoder[Crib] = deriveEncoder[Crib]
  implicit val cribDecoder: Decoder[Crib] = deriveDecoder[Crib]
}
