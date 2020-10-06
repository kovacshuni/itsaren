package com.hunorkovacs.itsaren

import zio._
import org.http4s.server.Server

package object simple {

  type Http4ServerLayer = Has[ZManaged[Runtime[ZEnv], Throwable, Server]]

  type RuntimeLayer = Has[Runtime[ZEnv]]

}
