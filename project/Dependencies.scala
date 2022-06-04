import sbt._

object Dependencies {

  private val CatsEffectVersion = "3.3.12"
  private val Http4sVersion     = "0.23.12"
  private val CirceVersion      = "0.14.2"

  val ItsarenSimple = Seq(
    "org.typelevel"          %% "cats-effect"         % CatsEffectVersion,
    "io.circe"               %% "circe-core"          % CirceVersion,
    "io.circe"               %% "circe-generic"       % CirceVersion,
    "io.circe"               %% "circe-parser"        % CirceVersion,
    "org.http4s"             %% "http4s-dsl"          % Http4sVersion,
    "org.http4s"             %% "http4s-ember-server" % Http4sVersion,
    "org.http4s"             %% "http4s-circe"        % Http4sVersion,
    ("com.github.pureconfig" %% "pureconfig"          % "0.17.1").cross(CrossVersion.for3Use2_13),
    "org.typelevel"          %% "log4cats-slf4j"      % "2.3.1",
    "ch.qos.logback"          % "logback-classic"     % "1.2.3"
  )

}
