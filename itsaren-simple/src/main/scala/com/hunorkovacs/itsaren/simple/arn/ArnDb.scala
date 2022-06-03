package com.hunorkovacs.itsaren.simple.arn

import cats.effect.IO
import java.util.UUID

trait ArnDb:

  def create(arnPost: ArnNoId): IO[Arn]

  def find(id: UUID): IO[Option[Arn]]

  def retrieveAll: IO[List[Arn]]

  def update(id: UUID, arnPost: ArnNoId): IO[Option[Arn]]

  def delete(id: UUID): IO[Option[Arn]]

