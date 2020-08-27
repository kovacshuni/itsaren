package com.hunorkovacs.itsaren.simple.crib

import com.hunorkovacs.itsaren.simple.crib.persistence.RefCribPersistenceService
import zio.{Has, Layer, Ref, Task, ZLayer}

package object crib {

  object Persistence {
    trait Service[A] {
      def create(cribPost: CribNoId): Task[Crib]
      def retrieve(id: String): Task[Option[Crib]]
      def retrieveAll: Task[List[Crib]]
      def update(id: String, cribPost: CribNoId): Task[Option[Crib]]
      def delete(id: String): Task[Option[Crib]]
    }
  }

  type CribPersistence = Has[Persistence.Service[Crib]]

  def test(cribs: Ref[Map[String, Crib]]): Layer[Nothing, CribPersistence] =
    ZLayer.fromEffect(Ref.make(Map.empty[String, Crib]).map(RefCribPersistenceService))
}
