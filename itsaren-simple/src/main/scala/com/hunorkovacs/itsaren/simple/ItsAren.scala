package com.hunorkovacs.itsaren.simple

import zio._

object ItsAren extends App {

  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] = {
    val serverResource = ZIO.access[HttpServer](_.httpServer)

    val used = serverResource
      .use { _ =>
        ZIO(println("Server online, accessible on port=8080 Press Ctrl-C (or send SIGINT) to stop"))
          .flatMap(_ => ZIO.never)
      }
      .as(ExitCode.success)
      .catchAll(_ => ZIO.succeed(ExitCode.failure))

      RouterLive
      LiveHttpServer
    used.provide()

  }
}
