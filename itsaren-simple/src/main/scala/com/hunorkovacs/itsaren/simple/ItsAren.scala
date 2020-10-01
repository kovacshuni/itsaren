package com.hunorkovacs.itsaren.simple

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._
import org.http4s.server.blaze.BlazeServerBuilder

object ItsAren extends App {

  def run(args: List[String]): zio.URIO[zio.ZEnv, ExitCode] = {
    ZIO
      .runtime[ZEnv]
      .flatMap { implicit runtime =>
        val serverResource = for {
          helloWorldService <- ZManaged.fromEffect(ZIO.access[Router](_.router.helloWorldService))
          server            <- BlazeServerBuilder[Task](runtime.platform.executor.asEC)
                                 .bindHttp(8080, "localhost")
                                 .withHttpApp(helloWorldService)
                                 .resource
                                 .toManagedZIO
        } yield server

        val used = serverResource
          .use { _ =>
            ZIO(println("Server online, accessible on port=8080 Press Ctrl-C (or send SIGINT) to stop"))
              .flatMap(_ => ZIO.never)
          }
          .as(ExitCode.success)
          .catchAll(_ => ZIO.succeed(ExitCode.failure))

        used.provide(RouterLiveObj)
      }
  }
}
