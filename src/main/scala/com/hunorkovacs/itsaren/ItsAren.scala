package com.hunorkovacs.itsaren

import crib._
import zio._
import zio.console.Console

import scala.collection.mutable

object ItsAren extends App {

  def run(args: List[String]): URIO[ZEnv, ExitCode] = {

    val program: ZIO[Http4Server with Console, Nothing, Nothing] =
      ZIO.never

    val initCribs = mutable.Map[String, Crib](
      ("af32635c-35c8-4b90-a012-b7576b8ba4c9", Crib("af32635c-35c8-4b90-a012-b7576b8ba4c9", "56th street", "0712345678")),
      ("24495031-ce2e-42a4-b500-4497502c0100", Crib("24495031-ce2e-42a4-b500-4497502c0100", "40th street", "0712345678"))
    )

    val cribRepoLayer: ZLayer[Any, Nothing, HCribRepo]                       = CribRepo.inMemCribRepo(initCribs)
    val httpServerLayer: ZLayer[ZEnv with HCribRepo, Throwable, Http4Server] = Http4Server.createHttp4sLayer
    val l1: ZLayer[ZEnv, Throwable, Http4Server]                             = cribRepoLayer >>> httpServerLayer

    program
      .provideLayer(l1 ++ Console.live)
      .exitCode
  }

}
