package com.hunorkovacs.itsaren.simple.arn

import cats.effect.IO

import scala.collection.mutable

object InMemArnDbService extends ArnDbService:

  private val arns = mutable.Map[String, Arn](
    ("af32635c-35c8-4b90-a012-b7576b8ba4c9", Arn("af32635c-35c8-4b90-a012-b7576b8ba4c9", "56th street", "0712345678")),
    ("24495031-ce2e-42a4-b500-4497502c0100", Arn("24495031-ce2e-42a4-b500-4497502c0100", "40th street", "0712345678"))
  )

  override def create(arnPost: ArnNoId): IO[Arn] = IO {
    val id = java.util.UUID.randomUUID.toString
    val arn = Arn(id, arnPost.address, arnPost.phone)
    arns.+=((id, arn))
    arn
  }

  override def retrieve(id: String): IO[Option[Arn]] = IO(arns.get(id))

  override def retrieveAll: IO[List[Arn]] = IO(arns.values.toList)

  override def update(id: String, arnPost: ArnNoId): IO[Option[Arn]] = IO{
    arns.get(id) map { arn =>
      val updatedArn = Arn(arn.id, arnPost.address, arnPost.phone)
      arns.put(arn.id, updatedArn)
      updatedArn
    }
  }

  override def delete(id: String): IO[Option[Arn]] = IO(arns.remove(id))
