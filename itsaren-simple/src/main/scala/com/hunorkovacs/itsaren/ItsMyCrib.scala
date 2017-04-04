package com.hunorkovacs.itsaren

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object ItsMyCrib extends App  {

  private val logger = LoggerFactory.getLogger(getClass)
  implicit private val sys = ActorSystem("itsmycrib")
  implicit private val mat = ActorMaterializer()
  import sys.dispatcher

  private val cribDbService = new CribDbService()
  private val router = new Router(cribDbService)

  private val httpBindingF = Http().bindAndHandle(router.route, "0.0.0.0", 8080)
  logger.info("Server online, accessible on port=8080")
  logger.info("Press Ctrl-C (or send SIGINT) to stop.")
  scala.sys addShutdownHook shutdown

  private def shutdown() = {
    logger.info("Exiting...")
    Await.ready(
      httpBindingF.flatMap(_.unbind())
        .flatMap(_ => Http().shutdownAllConnectionPools())
        .flatMap(_ => sys.terminate()), 5 seconds
    )
  }
}
