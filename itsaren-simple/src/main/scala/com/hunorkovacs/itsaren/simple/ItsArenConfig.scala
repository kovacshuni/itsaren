package com.hunorkovacs.itsaren.simple

import pureconfig.ConfigReader
import io.circe.Codec

case class ItsArenConfig(someParam: Int) derives Codec.AsObject

object ItsArenConfig {
  implicit val itsArenConfigReader: ConfigReader[ItsArenConfig] = ConfigReader[Int].map(n => new ItsArenConfig(n))
}
