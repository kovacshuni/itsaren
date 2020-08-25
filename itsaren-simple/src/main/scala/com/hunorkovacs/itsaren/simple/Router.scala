package com.hunorkovacs.itsaren.simple

import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.HttpOriginRange.*
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Headers`, `Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`}
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, ResponseEntity}
import akka.http.scaladsl.server._
import cats.effect.{ContextShift, IO}
import com.hunorkovacs.itsaren.simple.crib.Crib.CribNoId
import com.hunorkovacs.itsaren.simple.crib.InMemCribDbService
import com.hunorkovacs.itsaren.simple.message.Message
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe._
import io.circe.generic.auto._
import com.holidaycheck.akka.http.FMarshaller._

import scala.collection.immutable.Seq
import scala.concurrent.ExecutionContext

class Router(private val cribDbService: InMemCribDbService) extends Directives with FailFastCirceSupport {

  implicit private def outJson[T: Encoder](t: T): ResponseEntity =
    HttpEntity(`application/json`, Encoder[T].apply(t).spaces2)

  implicit val contextShift: ContextShift[IO] = IO.contextShift(ExecutionContext.global)

  val route: Route =
    respondWithDefaultHeaders(
      Seq(
        `Access-Control-Allow-Origin`(*)
      )
    ) {
      handleRejections(myRejectionHandler) {
        path("cribs") {
          get {
            complete {
              val cribs = cribDbService.retrieveAll
              cribs
            }
          } ~
            post {
              entity(as[CribNoId]) { cribPost =>
                complete {
                  cribDbService
                    .create(cribPost)
                    .map(created => HttpResponse(status = Created, entity = created))
                }
              }
            } ~
            options {
              respondWithDefaultHeaders(
                Seq(`Access-Control-Allow-Methods`(Seq(GET, OPTIONS, POST)), `Access-Control-Allow-Headers`(Seq("Content-Type")))
              ) {
                complete(OK)
              }
            }
        } ~
          path("cribs" / Remaining) { id =>
            get {
              complete {
                cribDbService.retrieve(id).map {
                  case Some(retrieved) => HttpResponse(status = Created, entity = retrieved)
                  case None            => HttpResponse(status = NotFound, entity = Message("Crib not found."))
                }
              }
            } ~
              put {
                entity(as[CribNoId]) { cribPost =>
                  complete {
                    cribDbService.update(id, cribPost).map {
                      case Some(updatedCrib) => HttpResponse(status = Created, entity = updatedCrib)
                      case None              => HttpResponse(status = NotFound, entity = Message("Crib not found, can't update."))
                    }
                  }
                }
              } ~
              delete {
                complete {
                  cribDbService.delete(id).map {
                    case Some(_) => HttpResponse(status = NoContent)
                    case None    => HttpResponse(status = NotFound, entity = Message("Crib not found, can't delete."))
                  }
                }
              } ~
              options {
                respondWithDefaultHeaders(
                  Seq(`Access-Control-Allow-Methods`(Seq(GET, DELETE, OPTIONS, PUT)), `Access-Control-Allow-Headers`(Seq("Content-Type")))
                ) {
                  complete(OK)
                }
              }
          }
      }
    }

  private def myRejectionHandler =
    RejectionHandler
      .newBuilder()
      .handleAll[MethodRejection] { methodRejections =>
        val names = methodRejections.map(_.supported.name)
        complete(HttpResponse(status = MethodNotAllowed, entity = Message(s"Can't do that! Supported: ${names mkString " or "}!")))
      }
      .handleNotFound {
        complete(HttpResponse(status = NotFound, entity = Message("Not here!")))
      }
      .handle {
        case r: MalformedRequestContentRejection => complete(HttpResponse(status = BadRequest, entity = Message(r.message)))
      }
      .result()
}
