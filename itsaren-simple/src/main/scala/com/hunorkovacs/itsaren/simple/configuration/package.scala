package com.hunorkovacs.itsaren.simple

import io.circe.Decoder
import io.circe.config.parser
import io.circe.generic.semiauto._
import zio._

package object configuration {

  type Configuration = zio.Has[Configuration.Service]

  case class AppConfig(api: ApiConfig, dbConfig: DbConfig)
  implicit val appConfigDecoder: Decoder[AppConfig] = deriveDecoder

  case class ApiConfig(endpoint: String, port: Int)
  case class DbConfig(url: String, user: String, password: String)

  val load: ZIO[Configuration, Throwable, AppConfig] =
    ZIO.accessM(_.get.load)

  val dbConfig: URIO[Has[DbConfig], DbConfig] = ZIO.access(_.get)

  object Configuration {

    trait Service {
      val load: Task[AppConfig]
    }

    trait Live extends Service {

      val load: Task[AppConfig] =
        Task.fromEither(parser.decode[AppConfig])
    }

    val live: ZLayer[Any, Throwable, Configuration] =
      ZLayer.succeed(new Live {})
  }

}
