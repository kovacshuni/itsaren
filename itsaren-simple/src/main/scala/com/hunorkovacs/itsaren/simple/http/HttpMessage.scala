package com.hunorkovacs.itsaren.simple.http

import io.circe.Codec

case class HttpMessage(message: String) derives Codec.AsObject
