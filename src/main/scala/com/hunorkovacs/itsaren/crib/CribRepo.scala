package com.hunorkovacs.itsaren.crib

import zio._
import cats.effect.IO

import scala.collection.mutable

object CribRepo {

  type CribRepo = Has[CribRepo.Service]

  trait Service {

    def create(cribPost: CribNoId): Task[Crib]

    def retrieve(id: String): Task[Option[Crib]]

    def retrieveAll: IO[List[Crib]]

    def update(id: String, cribPost: CribNoId): IO[Option[Crib]]

    def delete(id: String): IO[Option[Crib]]

  }

  def inMemCribRepo(cribs: mutable.Map[String, Crib]): ZLayer[Any, Nothing, CribRepo] =
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

        override def retrieveAll: IO[List[Crib]] =
          IO(cribs.values.toList)

        override def update(id: String, cribPost: CribNoId): IO[Option[Crib]] =
          IO {
            cribs.get(id) map { crib =>
              val updatedCrib = Crib(crib.id, cribPost.address, cribPost.phone)
              cribs.put(crib.id, updatedCrib)
              updatedCrib
            }
          }

        override def delete(id: String): IO[Option[Crib]] =
          IO(cribs.remove(id))

      }
    }

   def retrieve(id: String): ZIO[CribRepo.Service, Throwable, Option[Crib]] =
     ZIO.accessM[CribRepo.Service](_.retrieve(id))

}
