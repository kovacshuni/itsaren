package com.hunorkovacs

import org.http4s.server.Server
import zio._

package object itsaren {

  type HApp = HServer

  type HServer = Has[Server]

}
