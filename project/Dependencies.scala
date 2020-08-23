import sbt._

object Dependencies {

  private val AkkaVersion     = "2.6.8"
  private val AkkaHttpVersion = "10.2.0"
  private val CirceVersion    = "0.12.3"

  private val akka         = "com.typesafe.akka" %% "akka-actor"    % AkkaVersion
  private val circeCore    = "io.circe"          %% "circe-core"    % CirceVersion
  private val circeGeneric = "io.circe"          %% "circe-generic" % CirceVersion
  private val circeParser  = "io.circe"          %% "circe-parser"  % CirceVersion

  val Simple = Seq(
    akka,
    circeCore,
    circeGeneric,
    circeParser,
    "com.typesafe.akka" %% "akka-stream"          % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-core"       % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "org.slf4j"         % "slf4j-api"             % "1.7.30",
    "ch.qos.logback"    % "logback-classic"       % "1.2.3"
  )
}
