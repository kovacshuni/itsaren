package com.hunorkovacs

import org.http4s.server.Server
import zio._

package object itsaren {

  type Http4Server = Has[Server]

}
