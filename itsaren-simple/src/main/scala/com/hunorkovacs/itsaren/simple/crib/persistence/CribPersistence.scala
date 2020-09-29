package com.hunorkovacs.itsaren.simple.crib.persistence

import com.hunorkovacs.itsaren.simple.crib.Crib
import com.hunorkovacs.itsaren.simple.crib.CribNoId
import zio.Task

object CribPersistence {

  trait Service {

    def create(cribPost: CribNoId): Task[Crib]

    def retrieve(id: String): Task[Option[Crib]]

    def retrieveAll: Task[List[Crib]]

    def update(id: String, cribPost: CribNoId): Task[Option[Crib]]

    def delete(id: String): Task[Option[Crib]]

  }

}

trait CribPersistence {

  def cribPersistence: CribPersistence.Service

}
