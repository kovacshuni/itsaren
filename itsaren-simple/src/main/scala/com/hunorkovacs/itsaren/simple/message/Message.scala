package com.hunorkovacs.itsaren.simple.message

import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

case class Message(message: String)

object Message {
  implicit val messageEncoder: Encoder[Message] = deriveEncoder
}
