import sbt._

object Dependencies {

  private val AkkaVersion = "2.6.8"
  private val AkkaHttpVersion = "10.2.0"

  private val akka  = "com.typesafe.akka" %% "akka-actor" % AkkaVersion

  val Simple = Seq(
    akka,
    "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
    "com.typesafe.akka" %% "akka-http-core" % AkkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
    "org.slf4j" % "slf4j-api" % "1.7.30",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )
}
