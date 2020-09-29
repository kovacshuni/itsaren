package com.hunorkovacs.itsaren.simple.crib.persistence

import com.hunorkovacs.itsaren.simple.crib.Crib
import com.hunorkovacs.itsaren.simple.crib.CribNoId
import zio.Task
import scala.collection.mutable

class CribPersistenceLiveService extends CribPersistence.Service {

  private val cribs = mutable.Map[String, Crib](
    ("af32635c-35c8-4b90-a012-b7576b8ba4c9", Crib("af32635c-35c8-4b90-a012-b7576b8ba4c9", "56th street", "0712345678")),
    ("24495031-ce2e-42a4-b500-4497502c0100", Crib("24495031-ce2e-42a4-b500-4497502c0100", "40th street", "0712345678"))
  )

  def create(cribPost: CribNoId): Task[Crib] =
    Task {
      val id   = java.util.UUID.randomUUID.toString
      val crib = Crib(id, cribPost.address, cribPost.phone)
      cribs.+=((id, crib))
      crib
    }

  def retrieve(id: String): Task[Option[Crib]] = Task.effect(cribs.get(id))

  def retrieveAll: Task[List[Crib]] = Task(cribs.values.toList)

  def update(id: String, cribPost: CribNoId): Task[Option[Crib]] =
    Task {
      cribs.get(id) map { crib =>
        val updatedCrib = Crib(crib.id, cribPost.address, cribPost.phone)
        cribs.put(crib.id, updatedCrib)
        updatedCrib
      }
    }

  def delete(id: String): Task[Option[Crib]] = Task(cribs.remove(id))

}

trait CribPersistenceLive extends CribPersistence {

  override def cribPersistence: CribPersistence.Service = new CribPersistenceLiveService

}

object CribPersistenceLive extends CribPersistenceLive
