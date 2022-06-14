package com.hunorkovacs.itsaren.simple

import cats.effect.{ExitCode, IO, IOApp}
import cats.effect.kernel.Resource
import com.comcast.ip4s._
import com.hunorkovacs.itsaren.simple.arn.InMemArnDb
import com.hunorkovacs.itsaren.simple.http.HttpRouter
import com.typesafe.config.ConfigFactory
import io.circe.config.parser
import io.circe.syntax._
import org.http4s.ember.server._
import org.typelevel.log4cats._
import org.typelevel.log4cats.slf4j._

object ItsArenApp extends IOApp:

  override def run(args: List[String]): IO[ExitCode] =

    implicit val loggerFactory: LoggerFactory[IO] = Slf4jFactory[IO]
    val logger: SelfAwareStructuredLogger[IO]     = LoggerFactory[IO].getLogger

    Components("itsaren-sample")
      .use { components =>
        for {
          _ <- logger.info(s"Server online, accessible on port=${components.config.port} Press Ctrl-C (or send SIGINT) to stop")
          _ <- logger.info(
                 """Try: curl -i -XPOST -H"Content-Type:application/json" -d'{"address":"56th street","phone":"0712345678"}""" +
                   s"localhost:${components.config.port}/v1/arns"
               )

          _ <- logger.info(components.config.asJson.spaces2)

          // _ <- doStuffHere

          _ <- IO.never
        } yield ()
      }
      .as(ExitCode.Success)
