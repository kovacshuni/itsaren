package com.hunorkovacs.itsaren.simple

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._
import org.http4s._
import org.http4s.server.blaze.BlazeServerBuilder
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._

class Http4Server extends HttpServer.Service {

  override def createSever: ZManaged[ZEnv, Throwable, org.http4s.server.Server] = {
    val k = ZIO
      .runtime[ZEnv]
      .toManaged_
      .flatMap { implicit runtime =>
        BlazeServerBuilder[Task](runtime.platform.executor.asEC)
          .bindHttp(8080, "localhost")
          .withHttpApp(helloWorldService)
          .resource
          .toManagedZIO
      }
    k
  }

  private val dsl = Http4sDsl[Task]
  import dsl._

  private def helloWorldService =
    HttpRoutes
      .of[Task] {
        case GET -> Root / "hello" => Ok("Hi Joe")
      }
      .orNotFound
}

trait HttpServerLive extends HttpServer {

  def httpServer: HttpServer.Service = new Http4Server

}

object HttpServerLive extends HttpServerLive
