package com.hunorkovacs.itsaren.simple

import cats.effect.kernel.Resource
import com.hunorkovacs.itsaren.simple.arn.InMemArnDb
import io.circe.config.parser
import cats.effect.IO
import com.comcast.ip4s._
import com.typesafe.config.ConfigFactory
import org.http4s.ember.server.EmberServerBuilder
import com.hunorkovacs.itsaren.simple.http.HttpRouter
import cats.effect.kernel.Ref

class Components(
    val applicationReady: Ref[IO, Boolean],
    val config: ItsArenConfig)

object Components:

  def apply(appName: String) =
    for {
      given ItsArenConfig <- Resource.eval(parser.decodePathF[IO, ItsArenConfig](ConfigFactory.load, appName))
      dbService           <- Resource.eval(InMemArnDb.createSample)
      applicationReady    <- Resource.eval(Ref.of[IO, Boolean](false))
      healthServer        <- EmberServerBuilder
                               .default[IO]
                               .withHost(ipv4"0.0.0.0")
                               .withPort(Port.fromInt(implicitly[ItsArenConfig].healthPort).get)
                               .withHttpApp(HttpRouter.healthRoutes(applicationReady))
                               .build
      applicationServer   <- EmberServerBuilder
                               .default[IO]
                               .withHost(ipv4"0.0.0.0")
                               .withPort(Port.fromInt(implicitly[ItsArenConfig].port).get)
                               .withHttpApp(HttpRouter.applicationRoutes(dbService))
                               .build
    } yield new Components(
      applicationReady,
      implicitly[ItsArenConfig]
    )
