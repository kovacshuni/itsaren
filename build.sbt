scalaVersion := "2.13.3"

lazy val `itsaren-simple` = (project in file("itsaren-simple"))
  .enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)
  .settings(BuildConfig.commonSettings)
  .settings(
    Compile / mainClass := Some("com.hunorkovacs.itsaren.simple.ItsAren"),
    libraryDependencies ++= Dependencies.Simple
  )

lazy val `itsaren` = (project in file("."))
  .aggregate(
    `itsaren-simple`
  )
