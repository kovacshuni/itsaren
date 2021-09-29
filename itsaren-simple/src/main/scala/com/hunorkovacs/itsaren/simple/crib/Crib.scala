package com.hunorkovacs.itsaren.simple.crib

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class Crib(id: String, address: String, phone: String)

object Crib:
  implicit val cribEncoder: Encoder[Crib] = deriveEncoder
