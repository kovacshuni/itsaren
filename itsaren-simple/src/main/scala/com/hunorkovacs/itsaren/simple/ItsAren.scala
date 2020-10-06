package com.hunorkovacs.itsaren.simple

import zio._
import zio.console.Console
import zio.random.Random
import zio.clock.Clock
import org.http4s.server.Server

object ItsAren extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {

    val toProvide: ZIO[Http4ServerLayer, Nothing, ExitCode] =
      ZIO
        .accessM[Http4ServerLayer](_.get.useForever) //compile error
        .as(ExitCode.success)
        .catchAll(ZIO.succeed(ExitCode.failure))

    val runtimeLayer: ZLayer[Any, Nothing, RuntimeLayer]              = ZLayer.succeed(Runtime.default)
    val http4Layer: ZLayer[RuntimeLayer, Throwable, Http4ServerLayer] = HttpServer.createHttp4Layer
    val fullLayer: ZLayer[Any, Throwable, Http4ServerLayer]           = runtimeLayer >>> http4Layer

    val provided = toProvide.provideCustomLayer(fullLayer)

    provided //compile error
  }

}
