package com.hunorkovacs.itsaren

import zio._

package object crib {

  type CribRepo = Has[CribRepo.Service]

  type CribTask[A] = ZIO[CribRepo.Service, Throwable, A]

}
