package com.hunorkovacs.itsaren

import zio._
import zio.interop.catz._
import zio.interop.catz.implicits._

import org.http4s.server.Server
import org.http4s.server.blaze.BlazeServerBuilder

import crib.CribRoutes
import crib.CribRepo

object Http4Server {

  // type Task[A] = RIO[CribRepo, A]

  def createHttp4Server: ZManaged[ZEnv with CribRepo, Throwable, Server] =
    ZManaged.runtime[ZEnv].flatMap { implicit runtime: Runtime[ZEnv] =>
      BlazeServerBuilder[Task](runtime.platform.executor.asEC)
        .bindHttp(8080, "localhost")
        .withHttpApp(CribRoutes.cribRoutes)
        .resource
        .toManagedZIO
    }

  def createHttp4sLayer: ZLayer[ZEnv, Throwable, Http4Server] =
    ZLayer.fromManaged(createHttp4Server)

}
/*
found
Kleisli[[A(in value <local CribTask>)] CribTask[A(in value <local CribTask>)],     Request[CribTask], Response[[A(in value <local CribTask>)], CribTask[A(in value <local CribTask>)]]]
required:
Kleisli[[+A(in value <local Task>)] ZIO[Any, Throwable, A(in value <local Task>)], Request[[+A(in value <local Task>)] ZIO[Any, Throwable, A(in value <local Task>)]], Response[[+A(in value <local Task>)] ZIO[Any, Throwable, A(in value <local Task>)]]]
*/
