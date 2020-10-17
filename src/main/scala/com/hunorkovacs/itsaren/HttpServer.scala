package com.hunorkovacs.itsaren

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.clock.Clock

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import crib.CribRoutes
import crib.HCribRoutes
import crib._

object Http4Server {

  type AppTask[A] = ZIO[HServer with Clock, Throwable, A]

  def createHttp4Server: ZManaged[ZEnv with HCribRoutes, Throwable, Server] = {
    for {
      routes <- ZManaged.access[HCribRoutes](_.get)
      server <- ZManaged.runtime[ZEnv].flatMap { runtime: Runtime[ZEnv] =>
                  // type BuilderTask[A] = ZIO[HServer with Clock, Throwable, A]

                  // BlazeServerBuilder[BuilderTask](runtime.platform.executor.asEC)
                  //   .bindHttp(8080, "localhost")
                  //   .withHttpApp(rou2)
                  //   .resource
                  //   .toManagedZIO

                  ???
                }
    } yield server
  }

  def createHttp4sLayer: ZLayer[ZEnv with HCribRoutes, Throwable, HServer] =
    ZLayer.fromManaged(createHttp4Server)

}
