package com.hunorkovacs.itsaren.simple

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

object HttpServer {

  def createHttp4Server: ZManaged[ZEnv, Throwable, Server] =
    ZManaged.runtime[ZEnv].flatMap { implicit runtime: Runtime[ZEnv] =>
      BlazeServerBuilder[Task](runtime.platform.executor.asEC)
        .bindHttp(8080, "localhost")
        .withHttpApp(Routes.helloWorldService)
        .resource
        .toManagedZIO
    }

  def createHttp4Layer: ZLayer[ZEnv, Throwable, Has[Server]] =
    ZLayer.fromManaged(createHttp4Server)

}
