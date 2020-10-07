package com.hunorkovacs.itsaren

import zio._
import zio.interop.catz._

import org.http4s._
import org.http4s.dsl.Http4sDsl
import org.http4s.implicits._
import com.hunorkovacs.itsaren.crib.Crib

object Routes {

  val dsl = Http4sDsl[Task]
  import dsl._

  val helloWorldService = HttpRoutes
    .of[Task] {
      case GET -> Root / "cribs" / "af32635c-35c8-4b90-a012-b7576b8ba4c9" =>
        Ok(Crib("af32635c-35c8-4b90-a012-b7576b8ba4c9", "56th street", "0712345678"))
    }
    .orNotFound

}
