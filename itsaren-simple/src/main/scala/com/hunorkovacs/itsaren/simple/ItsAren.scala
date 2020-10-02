package com.hunorkovacs.itsaren.simple

import zio._
import zio.console.Console
import com.hunorkovacs.itsaren.simple.UserRepo.DBError

object ItsAren extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val user2: User = User(UserId(123), "Tommy")
    val makeUser: ZIO[Logging with UserRepo, DBError, Unit] = for {
      _ <- Logging.info(s"hi, inserting user")
      _ <- UserRepo.createUser(user2)
      _ <- Logging.info(s"user inserted, bye")
    } yield ()

    val horizontal: ZLayer[Console, Nothing, Logging with UserRepo] = Logging.consoleLogger ++ UserRepo.inMemoryRepo
    val fullLayer: Layer[Nothing, Logging with UserRepo] = Console.live >>> horizontal

    val provided = makeUser.provideLayer(fullLayer)

    provided.exitCode
  }

}
