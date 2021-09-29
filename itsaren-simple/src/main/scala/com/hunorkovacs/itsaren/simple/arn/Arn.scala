package com.hunorkovacs.itsaren.simple.arn

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class Arn(id: String, address: String, phone: String)

object Arn:
  implicit val arnEncoder: Encoder[Arn] = deriveEncoder
