package com.hunorkovacs.itsaren.simple

import io.circe.Codec

case class ItsArenConfig(
    healthPort: Int,
    port: Int)
    derives Codec.AsObject
