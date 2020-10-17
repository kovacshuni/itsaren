package com.hunorkovacs.itsaren.crib

import scala.collection.mutable
import zio._

object CribRepo {

  trait Service {

    def create(cribPost: CribNoId): Task[Crib]

    def retrieve(id: String): Task[Option[Crib]]

  }

  def inMemCribRepo(cribs: mutable.Map[String, Crib]): ZLayer[Any, Nothing, HCribRepo] =
    ZLayer.succeed {
      new Service {

        override def create(cribPost: CribNoId): Task[Crib] =
          Task {
            val id   = java.util.UUID.randomUUID.toString
            val crib = Crib(id, cribPost.address, cribPost.phone)
            cribs.+=((id, crib))
            crib
          }

        override def retrieve(id: String): Task[Option[Crib]] = Task(cribs.get(id))

      }
    }

  def retrieve(id: String): ZIO[HCribRepo, Throwable, Option[Crib]] =
    ZIO.accessM[HCribRepo](_.get.retrieve(id))

  def create(cribPost: CribNoId): ZIO[HCribRepo, Throwable, Crib] =
    ZIO.accessM[HCribRepo](_.get.create(cribPost))

}
