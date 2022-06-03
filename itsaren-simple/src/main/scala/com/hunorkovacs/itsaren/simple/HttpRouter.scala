package com.hunorkovacs.itsaren.simple

import cats.data.Kleisli
import cats.effect.IO
import com.hunorkovacs.itsaren.simple.arn.{ArnDbService, ArnNoId}
import com.hunorkovacs.itsaren.simple.message.Message
import io.circe.generic.auto._
import org.http4s.circe.CirceEntityEncoder._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.{EntityDecoder, HttpRoutes, Request, Response}

object HttpRouter:

  def applicationRoutes(arnDbService: ArnDbService): Kleisli[IO, Request[IO], Response[IO]] =
    HttpRoutes
      .of[IO] {
        case GET -> Root / "v1" / "arns" =>
          arnDbService.retrieveAll.flatMap(Ok(_))

        case GET -> Root / "v1" / "arns" / id =>
          arnDbService.retrieve(id).flatMap {
            case None      => NotFound()
            case Some(arn) => Ok(arn)
          }

        case req @ POST -> Root / "v1" / "arns" =>
          for {
            arnNoId <- req.as[ArnNoId]
            arn     <- arnDbService.create(arnNoId)
            resp    <- Ok(arn)
          } yield resp

        case req @ PUT -> Root / "v1" / "arns" / id =>
          for
            arnNoId <- req.as[ArnNoId]
            arn     <- arnDbService.update(id, arnNoId)
            resp    <- arn match
                         case Some(updatedArn) => Ok(updatedArn)
                         case None             => NotFound(Message("Arn not found, can't update."))
          yield resp

        case DELETE -> Root / "v1" / "arns" / id =>
          arnDbService.delete(id).flatMap {
            case None    => NotFound(Message("Arn not found, can't delete."))
            case Some(_) => NoContent()
          }
      }
      .orNotFound

  def healthRoutes: Kleisli[IO, Request[IO], Response[IO]] =
    HttpRoutes.of[IO] { case GET -> Root / "health" => Ok(Healthy()) }.orNotFound

  case class Healthy(message: String = "healthy")

  implicit val arnNoIdDecoder: EntityDecoder[IO, ArnNoId] = jsonOf[IO, ArnNoId]
