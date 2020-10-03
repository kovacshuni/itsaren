package com.hunorkovacs.itsaren.simple

import zio._
import zio.console.Console
import zio.random.Random
import zio.clock.Clock
import com.hunorkovacs.itsaren.simple.UserRepo.DBError

object ItsAren extends App {

  trait Connection {
    def close: UIO[Unit]
  }

  def makeConnection: UIO[Connection] =
    UIO(new Connection {
      def close = UIO(())
    })

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val makeUser: ZIO[Logging with UserRepo with Random with Clock, DBError, Unit] = for {
      userId    <- zio.random.nextLong.map(UserId.apply)
      createdAt <- zio.clock.currentDateTime.orDie
      user       = User(userId, "Tommy")
      _         <- Logging.info(s"hi, inserting user")
      _         <- UserRepo.createUser(user)
      _         <- Logging.info(s"user=$user inserted")
      retrUser  <- UserRepo.getUser(userId)
      _         <- Logging.info(s"user=$retrUser retrieved to be verified it is the saved one, bye")
    } yield ()

    val connectionLayer: Layer[Nothing, Has[Connection]] =
      ZLayer.fromAcquireRelease(makeConnection)(c => c.close)

    val postgresLayer: ZLayer[Has[Connection], Nothing, UserRepo] =
      ZLayer.fromFunction { hasC =>
        new UserRepo.Service {
          override def getUser(userId: UserId): IO[DBError, Option[User]] = UIO(Option(User(UserId(123), "Mike")))
          override def createUser(user: User): IO[DBError, Unit]          = UIO(())
        }
      }

    val horizontal: ZLayer[Console with Has[Connection], Nothing, Logging with UserRepo] =
      Logging.consoleLogger ++
        postgresLayer

    // UserRepo.inMemoryRepo

    val fullLayer: Layer[Nothing, Logging with UserRepo] = (connectionLayer ++ Console.live) >>> horizontal

    val provided: ZIO[ZEnv, DBError, Unit] = makeUser.provideCustomLayer(fullLayer)

    provided.exitCode
  }

}
