package com.hunorkovacs.itsaren.crib

import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio.interop.catz._

import Crib._
import com.hunorkovacs.itsaren.Http4Server.CribTask

object CribRoutes {

  val dsl = Http4sDsl[CribTask]
  import dsl._

  val cribRoutes = HttpRoutes
    .of[CribTask] {
      case GET -> Root / "cribs" / id   =>
        CribRepo.retrieve(id).flatMap {
          case None       => NotFound()
          case Some(crib) => Ok(crib)
        }

      case req @ POST -> Root / "cribs" =>
        (for {
          cribNoId <- req.as[CribNoId]
          created  <- CribRepo.create(cribNoId)
          resp     <- Ok(created)
        } yield resp)
          .catchAll {
            case dEx: Throwable => BadRequest(dEx.getMessage)
          }
    }
    .orNotFound

}
