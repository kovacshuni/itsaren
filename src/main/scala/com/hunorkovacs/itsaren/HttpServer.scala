package com.hunorkovacs.itsaren

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._
import zio.clock.Clock

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import crib.CribRoutes
import crib._

object Http4Server {

  type CribTask[A] = ZIO[HCribRepo, Throwable, A]

  type ZEnvWithCribRepo = ZEnv with Clock with HCribRepo

  def createHttp4Server: ZManaged[ZEnvWithCribRepo, Throwable, Server] =
    ZManaged.runtime[ZEnvWithCribRepo].flatMap { implicit runtime: Runtime[ZEnvWithCribRepo] =>


      val rou2 = CribRoutes.cribRoutes

      BlazeServerBuilder[CribTask](runtime.platform.executor.asEC)
        .bindHttp(8080, "localhost")
        .withHttpApp(rou2)
        .resource
        .toManagedZIO
    }

  def createHttp4sLayer: ZLayer[ZEnvWithCribRepo, Throwable, Http4Server] =
    ZLayer.fromManaged(createHttp4Server)

}
