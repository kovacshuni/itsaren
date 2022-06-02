package com.hunorkovacs.itsaren.simple

import cats.effect.{ExitCode, IO, IOApp}
import com.hunorkovacs.itsaren.simple.arn.InMemArnDbService
import org.http4s.blaze.server._
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._

import scala.concurrent.ExecutionContext.Implicits.global

object ItsArenApp extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =

    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory[IO]
    val logger: SelfAwareStructuredLogger[IO]     = LoggerFactory[IO].getLogger

    val httpServer = BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(Router.http4sRoutes(InMemArnDbService))
      .resource

    httpServer
      .use { _ =>
        for {
          _     <- logger.info("Server online, accessible on port=8080 Press Ctrl-C (or send SIGINT) to stop")
          _     <- logger.info("""Try: curl -i -XPOST -H"Content-Type:application/json" -d'{"address":"56th street","phone":"0712345678"}' localhost:8080/v1/arns""")
          never <- IO.never[Int]
        } yield never
      }
      .as(ExitCode.Success)
