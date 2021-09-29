package com.hunorkovacs.itsaren

import zio._
import org.http4s.HttpRoutes

package object crib {

  type HCribRepo = Has[CribRepo.Service]

  type CribTask[A] = ZIO[HCribRepo, Throwable, A]

  type HCribRoutes = Has[HttpRoutes[CribTask]]

}
