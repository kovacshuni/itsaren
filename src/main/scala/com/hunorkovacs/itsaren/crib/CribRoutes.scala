package com.hunorkovacs.itsaren.crib

import org.http4s._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe.CirceEntityDecoder._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio._
import zio.interop.catz._

import Crib._

object CribRoutes {

  val dsl = Http4sDsl[CribTask]
  import dsl._

  def createCribRoutes: HttpApp[CribTask] = HttpRoutes
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

  def createCribRepoLayer: ZLayer[HCribRepo, Throwable, HCribRoutes] =
    ZLayer.succeed(createCribRoutes)

}
