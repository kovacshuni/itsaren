package com.hunorkovacs.itsaren.simple

import zio._

object HttpServer {
  trait Service {
    def createSever: ZManaged[ZEnv, Throwable, org.http4s.server.Server]
  }
}

trait HttpServer {
  def httpServer: HttpServer.Service
}
