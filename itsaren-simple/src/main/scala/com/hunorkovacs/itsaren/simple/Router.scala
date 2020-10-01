package com.hunorkovacs.itsaren.simple

import cats.data.Kleisli
import zio.Task
import org.http4s._

object Router {

  trait Service {

    def helloWorldService: Kleisli[Task, Request[Task], Response[Task]]

  }

}

trait Router {

  def router: Router.Service

}
