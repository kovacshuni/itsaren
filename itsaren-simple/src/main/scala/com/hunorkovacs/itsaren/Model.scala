package com.hunorkovacs.itsaren

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, PrettyPrinter, RootJsonFormat}

case class CribPost(address: String, phone: String)

case class Crib(id: String, address: String, phone: String)

case class Message(message: String)

trait PrettyJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val printer = PrettyPrinter
  implicit val cribPostFormat: RootJsonFormat[CribPost] = DefaultJsonProtocol.jsonFormat2(CribPost)
  implicit val cribFormat: RootJsonFormat[Crib] = DefaultJsonProtocol.jsonFormat3(Crib)
  implicit val messageFormat: RootJsonFormat[Message] = DefaultJsonProtocol.jsonFormat1(Message)
}
