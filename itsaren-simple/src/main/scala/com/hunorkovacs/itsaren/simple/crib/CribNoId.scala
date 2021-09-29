package com.hunorkovacs.itsaren.simple.crib

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class CribNoId(address: String, phone: String)

object CribNoId:
  implicit val cribNoIdEncoder: Encoder[Crib] = deriveEncoder
  implicit val cribNoIdDecoder: Decoder[Crib] = deriveDecoder
