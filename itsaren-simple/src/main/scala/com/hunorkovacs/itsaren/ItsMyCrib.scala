package com.hunorkovacs.itsaren

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.Materializer
import org.slf4j.LoggerFactory

import scala.concurrent.Await
import scala.concurrent.duration._

object ItsMyCrib extends App {

  private val logger                     = LoggerFactory.getLogger(getClass)
  implicit private val sys: ActorSystem  = ActorSystem("itsmycrib")
  implicit private val mat: Materializer = Materializer(sys)
  import sys.dispatcher

  private val cribDbService = new CribDbService()
  private val router        = new Router(cribDbService)

  private val httpBindingF = Http().newServerAt("0.0.0.0", 8080).bindFlow(router.route)
  logger.info("Server online, accessible on port=8080")
  logger.info("Press Ctrl-C (or send SIGINT) to stop.")
  scala.sys addShutdownHook shutdown

  private def shutdown() = {
    logger.info("Exiting...")
    val term = for {
      httpBinding <- httpBindingF
      _           <- httpBinding.unbind
      _           <- Http().shutdownAllConnectionPools()
      _           <- sys.terminate()
    } yield ()
    Await.ready(term, 5 seconds)
  }
}
