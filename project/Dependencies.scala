import sbt._

object Dependencies {

  private val CatsVersion     = "2.1.4"
  private val AkkaVersion     = "2.6.8"
  private val AkkaHttpVersion = "10.2.0"
  private val CirceVersion    = "0.12.3"

  private val catsEffect    = "org.typelevel"     %% "cats-effect"     % CatsVersion
  private val akkaActor     = "com.typesafe.akka" %% "akka-actor"      % AkkaVersion
  private val akkaStream    = "com.typesafe.akka" %% "akka-stream"     % AkkaVersion
  private val akkaHttpCore  = "com.typesafe.akka" %% "akka-http-core"  % AkkaHttpVersion
  private val circeCore     = "io.circe"          %% "circe-core"      % CirceVersion
  private val circeGeneric  = "io.circe"          %% "circe-generic"   % CirceVersion
  private val circeParser   = "io.circe"          %% "circe-parser"    % CirceVersion
  private val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.34.0" // needs to match with akka-http

  private val easyAkkaMarshalling = "com.holidaycheck" %% "easy-akka-marshalling" % "1.0.0"

  private val slf4j         = "org.slf4j"          % "slf4j-api"       % "1.7.30"
  private val logback       = "ch.qos.logback"     % "logback-classic" % "1.2.3"

  val Simple = Seq(
    catsEffect,
    akkaActor,
    akkaStream,
    akkaHttpCore,
    circeCore,
    circeGeneric,
    circeParser,
    akkaHttpCirce,
    easyAkkaMarshalling,
    slf4j,
    logback
  )
}
