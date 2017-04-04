package com.hunorkovacs.itsaren

import akka.http.scaladsl.model.ContentTypes.`application/json`
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model.headers.HttpOriginRange.*
import akka.http.scaladsl.model.headers.{`Access-Control-Allow-Methods`, `Access-Control-Allow-Origin`, `Access-Control-Allow-Headers`, `Content-Type`}
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, ResponseEntity}
import akka.http.scaladsl.server._
import spray.json.JsonFormat

import scala.collection.immutable.Seq

class Router(private val cribDbService: CribDbService) extends Directives with PrettyJsonSupport {

  implicit private def outJson[T](t: T)(implicit f: JsonFormat[T]): ResponseEntity =
    HttpEntity(`application/json`, f.write(t).compactPrint)

  val route: Route =
    respondWithDefaultHeaders(Seq(
      `Access-Control-Allow-Origin`(*)
    )) {
      handleRejections(myRejectionHandler) {
        path("cribs") {
          get {
            complete {
              cribDbService.retrieveAll
            }
          } ~
          post {
            entity(as[CribPost]) { cribPost =>
              complete {
                HttpResponse(status = Created, entity = cribDbService.create(cribPost))
              }
            }
          } ~
          options {
            respondWithDefaultHeaders(Seq(
              `Access-Control-Allow-Methods`(Seq(GET, OPTIONS, POST)),
              `Access-Control-Allow-Headers`(Seq("Content-Type")))
            ) {
              complete(OK)
            }
          }
        } ~
        path("cribs" / Remaining ) { id =>
          get {
            complete {
              cribDbService.retrieve(id) match {
                case Some(c) => c
                case None => HttpResponse(status = NotFound, entity = Message("Crib not found."))
              }
            }
          } ~
          put {
            entity(as[CribPost]) { cribPost =>
              complete {
                cribDbService.update(id, cribPost) match {
                  case Some(updatedCrib) => HttpResponse(status = Created, entity = updatedCrib)
                  case None => HttpResponse(status = NotFound, entity = Message("Crib not found, can't update."))
                }
              }
            }
          } ~
          delete {
            complete {
              cribDbService.delete(id) match {
                case Some(_) => NoContent
                case None => HttpResponse(status = NotFound, entity = Message("Crib not found, can't delete."))
              }
            }
          } ~
          options {
            respondWithDefaultHeaders(Seq(
              `Access-Control-Allow-Methods`(Seq(GET, DELETE, OPTIONS, PUT)),
              `Access-Control-Allow-Headers`(Seq("Content-Type")))
            ) {
              complete(OK)
            }
          }
        }
      }
    }

  private def myRejectionHandler =
    RejectionHandler.newBuilder()
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
