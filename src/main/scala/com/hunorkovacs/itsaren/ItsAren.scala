package com.hunorkovacs.itsaren

import crib._
import zio._

import scala.collection.mutable

object ItsAren extends App {

  override def run(args: List[String]): URIO[ZEnv, ExitCode] = {

    val program: ZIO[HApp, Nothing, Nothing] =
      ZIO.never

    val initCribs = mutable.Map[String, Crib](
      ("af32635c-35c8-4b90-a012-b7576b8ba4c9", Crib("af32635c-35c8-4b90-a012-b7576b8ba4c9", "56th street", "0712345678")),
      ("24495031-ce2e-42a4-b500-4497502c0100", Crib("24495031-ce2e-42a4-b500-4497502c0100", "40th street", "0712345678"))
    )

    // format: off
    val cribRepoLayer  : ZLayer[Any, Nothing, HCribRepo]                   = CribRepo.inMemCribRepo(initCribs)
    val l3             : ZLayer[Any, Throwable, ZEnv with HCribRepo]       = ZEnv.live ++ cribRepoLayer
    val httpServerLayer: ZLayer[ZEnv with HCribRepo, Throwable, HServer]   = ??? //Http4Server.createHttp4sLayer
    val l2             : ZLayer[ZEnv, Throwable, HServer]                  = l3 >>> httpServerLayer
    val l1             : ZLayer[ZEnv, Throwable, HApp]                     = l2
    // format: on

    program
      .provideLayer(l1)
      .exitCode
  }

}
