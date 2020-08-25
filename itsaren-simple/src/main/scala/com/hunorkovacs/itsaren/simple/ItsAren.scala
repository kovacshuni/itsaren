package com.hunorkovacs.itsaren.simple

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import cats.effect.{ContextShift, ExitCode, IO, IOApp, Resource}
import com.hunorkovacs.itsaren.simple.crib.InMemCribDbService
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.Future

object ItsAren extends IOApp {

  implicit val contextShift2: ContextShift[IO] = contextShift

  override def run(args: List[String]): IO[ExitCode] = {
    val k = for {
      logger    <- Resource.liftF(IO(LoggerFactory.getLogger(getClass)))
      sys       <- createActorSystemResource(logger)
      boundHttp <- Resource.liftF(bindHttp(sys, logger))
    } yield boundHttp
    k.use(_ => IO.never).as(ExitCode.Success)
  }

  private def createActorSystemResource(logger: Logger): Resource[IO, ActorSystem] =
    Resource.make {
      IO {
        val sys = ActorSystem("itsaren")
        setupCoordinatedShutdown(sys, logger)
        sys
      }
    } { sys =>
      IO.fromFuture(IO(sys.terminate())).as(() => ())
    }

  private def setupCoordinatedShutdown(sys: ActorSystem, logger: Logger) =
    CoordinatedShutdown(sys).addCancellableTask(CoordinatedShutdown.PhaseBeforeServiceUnbind, "log-shutdown") { () =>
      Future {
        logger.info("Exiting...")
        Done
      }(sys.dispatcher)
    }

  private def bindHttp(sys: ActorSystem, logger: Logger): IO[Http.ServerBinding] =
    for {
      httpBinding <- IO.fromFuture {
                       implicit val imSys: ActorSystem = sys
                       val cribDbService               = new InMemCribDbService()
                       val router                      = new Router(cribDbService)
                       val httpBinding                 = Http(sys).newServerAt("0.0.0.0", 8080).bindFlow(router.route)
                       IO(httpBinding)
                     }
      _           <- IO(
                       logger.info(
                         "Server online, accessible on port=8080 " +
                           "Press Ctrl-C (or send SIGINT) to stop"
                       )
                     )
    } yield httpBinding
}
