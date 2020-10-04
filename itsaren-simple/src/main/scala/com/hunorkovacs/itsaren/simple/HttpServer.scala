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

  trait Service {
    def createServer: ZManaged[ZEnv, Throwable, Server]
  }

  val http4Server: ZLayer[Any, Nothing, HttpServer] =
    ZLayer.succeed {

      new Service {
        private val dsl = Http4sDsl[Task]
        import dsl._

        private val helloWorldService = HttpRoutes
          .of[Task] {
            case GET -> Root / "hello" => Ok("Hello, Joe")
          }
          .orNotFound

        def createServer: ZManaged[ZEnv, Throwable, Server] = {
          ZManaged.fromEffect(ZIO.runtime[ZEnv]).flatMap { rt =>
            implicit val implRuntime = rt

            BlazeServerBuilder[Task](implRuntime.platform.executor.asEC)
              .bindHttp(8080, "localhost")
              .withHttpApp(helloWorldService)
              .resource
              .toManagedZIO
          }
        }
      }
    }

  def createHttpServer: ZManaged[ZEnv, Throwable, Server] =
    ZManaged.accessM(s => s.get.)
}
