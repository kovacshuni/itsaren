package com.hunorkovacs

import org.http4s.server.Server
import zio._
// import crib._

package object itsaren {

  type Http4Server = Has[Server]

  // type ZEnvExtended = ZEnv with HCribRepo
  // type z = ZEnv with ZEnv

}
