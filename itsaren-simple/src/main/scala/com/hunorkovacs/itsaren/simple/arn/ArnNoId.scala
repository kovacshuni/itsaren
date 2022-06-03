package com.hunorkovacs.itsaren.simple.arn

import io.circe.Codec

case class ArnNoId(address: String, phone: String) derives Codec.AsObject
