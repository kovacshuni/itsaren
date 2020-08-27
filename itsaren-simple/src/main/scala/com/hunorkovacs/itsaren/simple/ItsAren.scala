package com.hunorkovacs.itsaren.simple

import cats.effect.{ExitCode, IO, IOApp}
import com.hunorkovacs.itsaren.simple.configuration.Configuration
import com.hunorkovacs.itsaren.simple.crib.InMemCribDbService
import org.http4s.server.blaze._
import org.slf4j.LoggerFactory
import zio.{Has, ZIO}

import scala.concurrent.ExecutionContext.Implicits.global

object ItsAren extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    val database: ZIO[Configuration, Throwable, DB] = ???
    val databas2: ZIO[Has[Configuration.Service], Throwable, DB] = ???

    val httpServer = BlazeServerBuilder[IO](global)
      .bindHttp(8080, "localhost")
      .withHttpApp(Router.http4sRoutes(InMemCribDbService))
      .resource

    httpServer
      .use { _ =>
        for {
          logger <- IO(LoggerFactory.getLogger(ItsAren.getClass))
          _      <- IO(logger.info("Server online, accessible on port=8080 Press Ctrl-C (or send SIGINT) to stop"))
          never  <- IO.never
        } yield never
      }
      .as(ExitCode.Success)
  }

}
