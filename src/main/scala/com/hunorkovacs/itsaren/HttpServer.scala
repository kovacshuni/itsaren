package com.hunorkovacs.itsaren

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.clock.Clock

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import org.http4s.HttpApp
import org.http4s.server.Router
import org.http4s.implicits._
import cats.effect._
import org.http4s.server.middleware.CORS

import crib.CribRoutes
import crib.HCribRoutes
import crib._

object Http4Server {

  type ServerClockTask[A] = ZIO[HServer with Clock, Throwable, A]

  def createHttp4Server: ZManaged[ZEnv with HCribRoutes, Throwable, Server] = {
    for {
      routes <- ZManaged.access[HCribRoutes](_.get)
      httpApp = Router[ServerClockTask]("/" -> routes).orNotFound
      server <- ZManaged.runtime[HServer with Clock].flatMap { implicit runtime: Runtime[HServer with Clock] =>
                  // type BuilderTask[A] = ZIO[HServer with Clock, Throwable, A]

                  BlazeServerBuilder[ServerClockTask](runtime.platform.executor.asEC)
                    .bindHttp(8080, "localhost")
                    .withHttpApp(???)
                    .resource
                    .toManagedZIO

                // ???
                }
    } yield server
  }

  def createTodoBackendHttp4Server[R <: Clock](
      httpApp: HttpApp[RIO[R, *]],
      port: Int
    ): ZManaged[R, Throwable, Server] = {
    type Task[A] = RIO[R, A]
    ZManaged.runtime[R].flatMap { implicit rts =>
      BlazeServerBuilder
        .apply[Task](rts.platform.executor.asEC)
        .bindHttp(port, "0.0.0.0")
        .withHttpApp(CORS(httpApp))
        .resource
        .toManagedZIO
    }
  }

  def createHttp4sLayer: ZLayer[ZEnv with HCribRoutes, Throwable, HServer] =
    ZLayer.fromManaged(createHttp4Server)

}
