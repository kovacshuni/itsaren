lazy val root =
  project
    .in(file("."))
    .settings(
      organization := "com.hunorkovacs",
      name := "itsaren",
      version := "0.0.1-SNAPSHOT",
      scalaVersion := "2.13.3",
      resolvers += Resolver.sonatypeRepo("snapshots"),
      libraryDependencies ++= Seq(
        "org.http4s"    %% "http4s-blaze-server" % versions.http4s,
        "org.http4s"    %% "http4s-dsl"          % versions.http4s,
        "dev.zio"       %% "zio"                 % "1.0.2",
        "dev.zio"       %% "zio-interop-cats"    % "2.2.0.1",
        "org.http4s"    %% "http4s-circe"        % versions.http4s,
        "io.circe"      %% "circe-generic"       % "0.13.0",
        "org.slf4j"      % "slf4j-api"           % "1.7.30",
        "ch.qos.logback" % "logback-classic"     % "1.2.3"
      )
    )

lazy val versions = new {
  val http4s = "1.0.0-M4"
}
