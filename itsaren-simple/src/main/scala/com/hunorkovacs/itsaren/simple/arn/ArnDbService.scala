package com.hunorkovacs.itsaren.simple.arn

import cats.effect.IO

trait ArnDbService:

  def create(arnPost: ArnNoId): IO[Arn]

  def retrieve(id: String): IO[Option[Arn]]

  def retrieveAll: IO[List[Arn]]

  def update(id: String, arnPost: ArnNoId): IO[Option[Arn]]

  def delete(id: String): IO[Option[Arn]]

