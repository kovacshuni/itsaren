package com.hunorkovacs.itsaren.simple

import zio._
import org.http4s.server.Server

object ItsAren extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {
    val httpServerLayer = HttpServer.createHttp4Layer

    val program: ZIO[Has[Server], Nothing, ExitCode] =
      ZIO.never


    val runnable = program
      .provideLayer(httpServerLayer)
      .exitCode

    runnable
  }

}
