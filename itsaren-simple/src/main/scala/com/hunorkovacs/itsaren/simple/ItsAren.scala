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

    val horizontal: ZLayer[Console, Nothing, Logging with UserRepo with Random with Clock] =
      Logging.consoleLogger ++
        UserRepo.inMemoryRepo ++
        Random.live ++
        Clock.live

    val fullLayer: Layer[Nothing, Logging with UserRepo with Random with Clock] = Console.live >>> horizontal

    val provided = makeUser.provideLayer(fullLayer)

    provided.exitCode
  }

}
