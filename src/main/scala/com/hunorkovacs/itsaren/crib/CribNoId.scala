package com.hunorkovacs.itsaren.crib

import zio.Task
import zio.json._
import zio.interop.catz._
// import zio.interop.catz.implicits._
import org.http4s.EntityDecoder

// import com.hunorkovacs.itsaren.DeserializationException
import com.hunorkovacs.itsaren.DeserializationException

case class CribNoId(address: String, phone: String)

object CribNoId {
  implicit val cribNoIdEncoder: JsonEncoder[CribNoId] = DeriveJsonEncoder.gen[CribNoId]
  implicit val cribNoIdDecoder: JsonDecoder[CribNoId] = DeriveJsonDecoder.gen[CribNoId]

  implicit val cribNoIdEntityDecoder: EntityDecoder[Task, CribNoId] =
    EntityDecoder
      .text[Task]
      .map { crib =>
        crib
          .fromJson[CribNoId]
          .fold(
            msg => throw new DeserializationException(msg),
            identity
          )
      }
      .orElse(EntityDecoder.error(new DeserializationException("hi error")))

}
