package com.hunorkovacs.itsaren.simple.crib

import cats.effect.IO
import com.hunorkovacs.itsaren.simple.crib.Crib.CribNoId

trait CribDbService {

  def create(cribPost: CribNoId): IO[Crib]

  def retrieve(id: String): IO[Option[Crib]]

  def retrieveAll: IO[Iterable[Crib]]

  def update(id: String, cribPost: CribNoId): IO[Option[Crib]]

  def delete(id: String): IO[Option[Crib]]

}
