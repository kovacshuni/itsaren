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

  def createHttp4Server: ZManaged[ZEnv with HCribRepo, Throwable, Server] =
    ZManaged.runtime[ZEnv with HCribRepo].flatMap { _ => // runtime: Runtime[ZEnv] =>

      // val rou2 = CribRoutes.cribRoutes

      // BlazeServerBuilder[CribTask](runtime.platform.executor.asEC)
      //   .bindHttp(8080, "localhost")
      //   .withHttpApp(rou2)
      //   .resource
      //   .toManagedZIO

      ???
    }

  def createHttp4sLayer: ZLayer[ZEnv with HCribRepo, Throwable, HServer] =
    ZLayer.fromManaged(createHttp4Server)

}
