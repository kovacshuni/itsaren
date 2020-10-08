package com.hunorkovacs.itsaren.crib

import io.circe._
import io.circe.generic.semiauto._
import org.http4s.EntityDecoder
import org.http4s.circe._
import zio.Task
import zio.interop.catz._
// import org.http4s.circe.CirceEntityEncoder._

case class Crib(id: String, address: String, phone: String)

object Crib {
  implicit val cribEncoder: Encoder[Crib] = deriveEncoder[Crib]
  implicit val cribDecoder: Decoder[Crib] = deriveDecoder[Crib]

  implicit val ed: EntityDecoder[Task, Crib] = jsonOf[Task, Crib]
  // implicit val ee: EntityEncoder[Task, Crib] =
}
