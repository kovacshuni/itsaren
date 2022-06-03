package com.hunorkovacs.itsaren.simple

import cats.effect.{ExitCode, IO, IOApp}
import com.comcast.ip4s._
import com.hunorkovacs.itsaren.simple.arn.InMemArnDb
import com.hunorkovacs.itsaren.simple.http.HttpRouter
import org.http4s.ember.server._
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._

import scala.concurrent.ExecutionContext.Implicits.global
import cats.effect.kernel.Resource

object ItsArenApp extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =

    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory[IO]
    val logger: SelfAwareStructuredLogger[IO]     = LoggerFactory[IO].getLogger

    (for
      dbService         <- Resource.eval(InMemArnDb.createSample)
      healthServer      <- EmberServerBuilder
                             .default[IO]
                             .withHost(ipv4"0.0.0.0")
                             .withPort(port"8083")
                             .withHttpApp(HttpRouter.healthRoutes)
                             .build
      applicationServer <- EmberServerBuilder
                             .default[IO]
                             .withHost(ipv4"0.0.0.0")
                             .withPort(port"8080")
                             .withHttpApp(HttpRouter.applicationRoutes(dbService))
                             .build
    yield ())
      .use { _ =>
        for {
          _ <- logger.info("Server online, accessible on port=8080 Press Ctrl-C (or send SIGINT) to stop")
          _ <- logger.info("""Try: curl -i -XPOST -H"Content-Type:application/json" -d'{"address":"56th street","phone":"0712345678"}' localhost:8080/v1/arns""")
          _ <- IO.never
        } yield ()
      }
      .as(ExitCode.Success)
