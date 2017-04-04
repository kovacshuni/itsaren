package com.hunorkovacs.itsaren

import scala.collection.mutable

class CribDbService {

  private val cribs = mutable.Map[String, Crib](
    ("af32635c-35c8-4b90-a012-b7576b8ba4c9", Crib("af32635c-35c8-4b90-a012-b7576b8ba4c9", "56th street", "0712345678")),
    ("24495031-ce2e-42a4-b500-4497502c0100", Crib("24495031-ce2e-42a4-b500-4497502c0100", "40th street", "0712345678"))
  )

  def create(cribPost: CribPost): Crib = {
    val id = java.util.UUID.randomUUID.toString
    val crib = Crib(id, cribPost.address, cribPost.phone)
    cribs.+=((id, crib))
    crib
  }

  def delete(id: String): Option[Crib] = cribs.remove(id)

  def update(id: String, cribPost: CribPost): Option[Crib] = {
    cribs.get(id) map { crib =>
      val updatedCrib = Crib(crib.id, cribPost.address, cribPost.phone)
      cribs.put(crib.id, updatedCrib)
      updatedCrib
    }
  }

  def retrieve(id: String): Option[Crib] = cribs.get(id)

  def retrieveAll: Iterable[Crib] = cribs.values
}
