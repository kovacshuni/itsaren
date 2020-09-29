package com.hunorkovacs.itsaren.simple

import zio._
import zio.console._
import zio.interop.catz._
import zio.interop.catz.implicits._
import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

import org.slf4j.LoggerFactory
import org.slf4j.Logger

object ItsAren extends App {

  private val dsl = Http4sDsl[Task]
  import dsl._

  private val helloWorldService = HttpRoutes
    .of[Task] {
      case GET -> Root / "hello" => Ok("Hi Joe")
    }
    .orNotFound

  private def f(c: Console): Logger = LoggerFactory.getLogger(ItsAren.getClass)

  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] =
    ZIO
      .runtime[ZEnv]
      .flatMap { implicit runtime =>
        (for {
          server <- BlazeServerBuilder[Task](runtime.platform.executor.asEC)
                      .bindHttp(8080, "localhost")
                      .withHttpApp(helloWorldService)
                      .resource
                      .toManagedZIO
          logger <- ZManaged.fromFunction(f)
        } yield (server, logger))
          .use {
            case (_, logger) =>
              ZIO(logger.info("Server online, accessible on port=8080 Press Ctrl-C (or send SIGINT) to stop"))
                .flatMap(_ => ZIO.never)
          }
          .as(ExitCode.success)
          .catchAllCause(err => putStrLn(err.prettyPrint).as(ExitCode.failure))
      }

}
