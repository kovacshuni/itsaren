scalaVersion := "2.13.3"

lazy val `itsaren-simple` = (project in file("itsaren-simple"))
  .settings(BuildConfig.commonSettings)
  .settings(
    libraryDependencies ++= Dependencies.ItsarenSimple
  )

lazy val `itsaren` = (project in file("."))
  .aggregate(
    `itsaren-simple`
  )
