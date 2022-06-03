package com.hunorkovacs.itsaren.simple.arn

import cats.effect.IO
import cats.effect.kernel.Ref
import java.util.UUID

class InMemArnDb(db: Ref[IO, Map[UUID, Arn]]) extends ArnDb:

  override def create(arnPost: ArnNoId): IO[Arn] =
    db.modify { arns =>
      val id         = java.util.UUID.randomUUID
      val createdArn = Arn(id, arnPost.address, arnPost.phone)
      (arns.+((id, createdArn)), createdArn)
    }

  override def find(id: UUID): IO[Option[Arn]] =
    db.get.map(_.get(id))

  override def retrieveAll: IO[List[Arn]] =
    db.get.map(_.values.toList)

  override def update(id: UUID, arnPost: ArnNoId): IO[Option[Arn]] = db.modify { arns =>
    arns.get(id) match {
      case None    => (arns, None)
      case Some(_) =>
        val updatedArn  = Arn(id, arnPost.address, arnPost.phone)
        val updatedArns = arns.removed(id).+((id, updatedArn))
        (updatedArns, Some(updatedArn))
    }
  }

  override def delete(id: UUID): IO[Option[Arn]] = db.modify { arns =>
    arns.get(id).match {
      case None      => (arns, None)
      case Some(arn) => (arns.removed(id), Some(arn))
    }
  }

object InMemArnDb:

  def createSample: IO[InMemArnDb] =
    Ref
      .of[IO, Map[UUID, Arn]](
        Map[UUID, Arn](
          UUID.fromString("af32635c-35c8-4b90-a012-b7576b8ba4c9") -> Arn(UUID.fromString("af32635c-35c8-4b90-a012-b7576b8ba4c9"), "56th street", "0712345678"),
          UUID.fromString("24495031-ce2e-42a4-b500-4497502c0100") -> Arn(UUID.fromString("24495031-ce2e-42a4-b500-4497502c0100"), "40th street", "0712345678")
        )
      )
      .map(InMemArnDb(_))
