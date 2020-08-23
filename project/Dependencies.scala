import sbt._

object Dependencies {

  private val AkkaVersion     = "2.6.8"
  private val AkkaHttpVersion = "10.2.0"
  private val CirceVersion    = "0.12.3"

  private val akkaActor     = "com.typesafe.akka" %% "akka-actor"      % AkkaVersion
  private val akkaStream    = "com.typesafe.akka" %% "akka-stream"     % AkkaVersion
  private val akkaHttpCore  = "com.typesafe.akka" %% "akka-http-core"  % AkkaHttpVersion
  private val circeCore     = "io.circe"          %% "circe-core"      % CirceVersion
  private val circeGeneric  = "io.circe"          %% "circe-generic"   % CirceVersion
  private val circeParser   = "io.circe"          %% "circe-parser"    % CirceVersion
  private val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % "1.34.0"// exclude ("com.typesafe.akka", "akka-http_2.12")

  val Simple = Seq(
    akkaActor,
    akkaStream,
    akkaHttpCore,
    circeCore,
    circeGeneric,
    circeParser,
    akkaHttpCirce,
    "org.slf4j"      % "slf4j-api"       % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}
