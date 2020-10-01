package com.hunorkovacs.itsaren.simple

import cats.data.Kleisli
import zio.Task
import org.http4s._

// import cats.data.Kleisli
// import cats.effect.IO
// import com.hunorkovacs.itsaren.simple.crib.{CribDbService, CribNoId}
// import com.hunorkovacs.itsaren.simple.message.Message
// import io.circe.generic.auto._
// import org.http4s.circe.CirceEntityEncoder._
// import org.http4s.circe._
// import org.http4s.dsl.io._
// import org.http4s.implicits._
// import org.http4s.{EntityDecoder, HttpRoutes, Request, Response}

object Router {

  trait Service {

    def helloWorldService: Kleisli[Task, Request[Task], Response[Task]]

  }



  // def http4sRoutes(cribDbService: CribDbService): Kleisli[IO, Request[IO], Response[IO]] =
  //   HttpRoutes
  //     .of[IO] {
  //       case GET -> Root / "cribs"            =>
  //         cribDbService.retrieveAll.flatMap(Ok(_))

  //       case GET -> Root / "cribs" / id       =>
  //         cribDbService.retrieve(id).flatMap {
  //           case None       => NotFound()
  //           case Some(crib) => Ok(crib)
  //         }

  //       case req @ POST -> Root / "cribs"     =>
  //         for {
  //           cribNoId <- req.as[CribNoId]
  //           crib     <- cribDbService.create(cribNoId)
  //           resp     <- Ok(crib)
  //         } yield resp

  //       case req @ PUT -> Root / "cribs" / id =>
  //         for {
  //           cribNoId <- req.as[CribNoId]
  //           crib     <- cribDbService.update(id, cribNoId)
  //           resp     <- crib match {
  //                         case Some(updatedCrib) => Ok(updatedCrib)
  //                         case None              => NotFound(Message("Crib not found, can't update."))
  //                       }
  //         } yield resp

  //       case DELETE -> Root / "cribs" / id    =>
  //         cribDbService.delete(id).flatMap {
  //           case None    => NotFound(Message("Crib not found, can't delete."))
  //           case Some(_) => NoContent()
  //         }
  //     }
  //     .orNotFound

  // implicit val cribNoIdDecoder: EntityDecoder[IO, CribNoId] = jsonOf[IO, CribNoId]
}

trait Router {

  def router: Router.Service

}
