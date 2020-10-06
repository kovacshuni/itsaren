package com.hunorkovacs.itsaren.simple

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

object HttpServer {

  def createHttp4Server: ZManaged[Runtime[ZEnv], Throwable, Server] = {

      ZManaged.accessManaged { implicit runtime: Runtime[ZEnv] =>

        BlazeServerBuilder[Task](runtime.platform.executor.asEC)
          .bindHttp(8080, "localhost")
          .withHttpApp(Routes.helloWorldService)
          .resource
          .toManagedZIO
      }

  }

  def createHttp4Layer: ZLayer[RuntimeLayer, Throwable, Http4ServerLayer] = {

    ZLayer.succeed(createHttp4Server)

  }

}

object Routes {

  val dsl = Http4sDsl[Task]
  import dsl._

  val helloWorldService = HttpRoutes
    .of[Task] {
      case GET -> Root / "hello" => Ok("Hello, Joe")
    }
    .orNotFound

}
