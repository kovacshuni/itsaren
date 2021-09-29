import sbt._

object Dependencies {

  private val CatsVersion     = "3.2.9"
  private val Http4sVersion   = "0.23.4"
  private val CirceVersion    = "0.14.1"

  val ItsarenSimple = Seq(
    "org.typelevel"     %% "cats-effect"           % CatsVersion,
    "io.circe"          %% "circe-core"            % CirceVersion,
    "io.circe"          %% "circe-generic"         % CirceVersion,
    "io.circe"          %% "circe-parser"          % CirceVersion,
    "org.http4s"        %% "http4s-dsl"            % Http4sVersion,
    "org.http4s"        %% "http4s-blaze-server"   % Http4sVersion,
    "org.http4s"        %% "http4s-circe"          % Http4sVersion,
    "org.slf4j"          % "slf4j-api"             % "1.7.30",
    "ch.qos.logback"     % "logback-classic"       % "1.2.3"
  )

}
