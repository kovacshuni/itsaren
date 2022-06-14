package com.hunorkovacs.itsaren.simple


import io.circe.Codec

case class ItsArenConfig(someParam: Int) derives Codec.AsObject
