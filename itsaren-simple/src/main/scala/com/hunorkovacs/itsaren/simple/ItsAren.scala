package com.hunorkovacs.itsaren.simple

import zio._
import zio.console.Console
import zio.random.Random
import zio.clock.Clock
import com.hunorkovacs.itsaren.simple.UserRepo.DBError

object ItsAren extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val makeUser: ZIO[Logging with UserRepo with Random with Clock, DBError, Unit] = for {
      userId    <- zio.random.nextLong.map(UserId.apply)
      createdAt <- zio.clock.currentDateTime.orDie
      user       = User(userId, "Tommy")
      _         <- Logging.info(s"hi, inserting user")
      _         <- UserRepo.createUser(user)
      _         <- Logging.info(s"user=$user inserted, bye")
    } yield ()

    val horizontal: ZLayer[Console, Nothing, Logging with UserRepo] =
      Logging.consoleLogger ++
        UserRepo.inMemoryRepo

    val fullLayer: Layer[Nothing, Logging with UserRepo] = Console.live >>> horizontal

    val provided: ZIO[ZEnv, DBError, Unit] = makeUser.provideCustomLayer(fullLayer)

    provided.exitCode
  }

}
