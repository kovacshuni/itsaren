package com.hunorkovacs.itsaren.simple.arn

import io.circe.{Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class ArnNoId(address: String, phone: String)

object ArnNoId:
  implicit val arnNoIdEncoder: Encoder[Arn] = deriveEncoder
  implicit val arnNoIdDecoder: Decoder[Arn] = deriveDecoder
