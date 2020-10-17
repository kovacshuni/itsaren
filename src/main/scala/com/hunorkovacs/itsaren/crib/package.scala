package com.hunorkovacs.itsaren

import zio._

package object crib {

  type HCribRepo = Has[CribRepo.Service]

}
