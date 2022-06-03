package com.hunorkovacs.itsaren.simple.arn

import io.circe.Codec
import java.util.UUID

case class Arn(id: UUID, address: String, phone: String) derives Codec.AsObject
