package com.hunorkovacs.itsaren.simple

import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import zio._
import zio.interop.catz._
import cats.data.Kleisli

class FourService extends Router.Service {

  private val dsl = Http4sDsl[Task]
  import dsl._

  def helloWorldService: Kleisli[Task, Request[Task], Response[Task]] =
    HttpRoutes
      .of[Task] {
        case GET -> Root / "hello" => Ok("Hi Joe")
      }
      .orNotFound
}

trait RouterLive extends Router {

  override def router: FourService = new FourService

}

object RouterLive extends RouterLive {

  def apply: Router = new RouterLive {}
}
