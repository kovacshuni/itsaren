package com.hunorkovacs.itsaren.simple

import cats.effect.kernel.Resource
import com.hunorkovacs.itsaren.simple.arn.InMemArnDb
import io.circe.config.parser
import cats.effect.IO
import com.comcast.ip4s._
import com.typesafe.config.ConfigFactory
import org.http4s.ember.server.EmberServerBuilder
import com.hunorkovacs.itsaren.simple.http.HttpRouter

class Components(implicit val config: ItsArenConfig)

object Components:

  def apply(appName: String) =
    for {
      given ItsArenConfig <- Resource.eval(parser.decodePathF[IO, ItsArenConfig](ConfigFactory.load, appName))
      dbService           <- Resource.eval(InMemArnDb.createSample)
      healthServer        <- EmberServerBuilder
                               .default[IO]
                               .withHost(ipv4"0.0.0.0")
                               .withPort(Port.fromInt(implicitly[ItsArenConfig].healthPort).get)
                               .withHttpApp(HttpRouter.healthRoutes)
                               .build
      applicationServer   <- EmberServerBuilder
                               .default[IO]
                               .withHost(ipv4"0.0.0.0")
                               .withPort(Port.fromInt(implicitly[ItsArenConfig].port).get)
                               .withHttpApp(HttpRouter.applicationRoutes(dbService))
                               .build
    } yield new Components
