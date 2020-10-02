package com.hunorkovacs.itsaren.simple

import zio._

object UserRepo {

  trait Service {

    def getUser(userId: UserId): IO[DBError, Option[User]]

    def createUser(user: User): IO[DBError, Unit]

  }

  lazy val unimplementedRepo: ZLayer[Any, Nothing, UserRepo] = ZLayer.succeed(???)

  lazy val inMemoryRepo: Layer[Nothing, UserRepo] = ZLayer.succeed(
    new Service {

      private val mem = Ref.make(Map[UserId, User]())

      def getUser(userId: UserId): IO[DBError, Option[User]] =
        mem.flatMap(me => me.get).map(m => m.get(userId))

      def createUser(user: User): IO[DBError, Unit] =
        mem.flatMap(me => me.update(m => m.+(user.id -> user)))

    }
  )

  def getUser(userId: UserId): ZIO[UserRepo, DBError, Option[User]] =
    ZIO.accessM(_.get.getUser(userId))

  def createUser(user: User): ZIO[UserRepo, DBError, Unit] =
    ZIO.accessM(_.get.createUser(user))

  case class DBError() extends RuntimeException

}


