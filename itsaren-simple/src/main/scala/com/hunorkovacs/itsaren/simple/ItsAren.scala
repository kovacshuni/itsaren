package com.hunorkovacs.itsaren.simple

import akka.Done
import akka.actor.{ActorSystem, CoordinatedShutdown}
import akka.http.scaladsl.Http
import akka.stream.Materializer
import com.hunorkovacs.itsaren.simple.crib.InMemCribDbService
import org.slf4j.LoggerFactory

import scala.concurrent.Future

object ItsAren extends App {

  private val logger                     = LoggerFactory.getLogger(getClass)
  implicit private val sys: ActorSystem  = ActorSystem("itsmycrib")
  implicit private val mat: Materializer = Materializer(sys)
  import sys.dispatcher

  private val cribDbService = new InMemCribDbService()
  private val router        = new Router(cribDbService)

  private val httpBindingF = Http().newServerAt("0.0.0.0", 8080).bindFlow(router.route)
  logger.info("Server online, accessible on port=8080")
  logger.info("Press Ctrl-C (or send SIGINT) to stop.")

  CoordinatedShutdown(sys).addCancellableTask(CoordinatedShutdown.PhaseBeforeServiceUnbind, "log-shutdown") { () =>
    Future {
      logger.info("Exiting...")
      Done
    }
  }

}
