package com.hunorkovacs.itsaren.simple.crib.persistence

import com.hunorkovacs.itsaren.simple.crib.{Crib, CribNoId}
import com.hunorkovacs.itsaren.simple.crib.crib.Persistence
import zio.{Ref, Task}

case class RefCribPersistenceService(cribs: Ref[Map[String, Crib]]) extends Persistence.Service[Crib] {

  override def create(cribPost: CribNoId): Task[Crib] = {
    val id   = java.util.UUID.randomUUID.toString
    val crib = Crib(id, cribPost.address, cribPost.phone)
    cribs.update(m => m.+((id, crib))).map(_ => crib)
  }

  override def retrieve(id: String): Task[Option[Crib]] = ???

  override def retrieveAll: Task[List[Crib]] = ???

  override def update(id: String, cribPost: CribNoId): Task[Option[Crib]] = ???

  override def delete(id: String): Task[Option[Crib]] = ???
}
