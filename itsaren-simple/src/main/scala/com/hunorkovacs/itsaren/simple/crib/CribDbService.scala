package com.hunorkovacs.itsaren.simple.crib

import com.hunorkovacs.itsaren.simple.crib.Crib.CribNoId

trait CribDbService {

  def create(cribPost: CribNoId): Crib

  def retrieve(id: String): Option[Crib]

  def retrieveAll: Iterable[Crib]

  def update(id: String, cribPost: CribNoId): Option[Crib]

  def delete(id: String): Option[Crib]

}
