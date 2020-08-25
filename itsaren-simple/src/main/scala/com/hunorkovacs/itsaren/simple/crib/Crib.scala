package com.hunorkovacs.itsaren.simple.crib

case class Crib(id: String, address: String, phone: String)

object Crib {

  case class CribNoId(address: String, phone: String)

}
