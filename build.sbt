
lazy val `itsaren-simple` = (project in file("itsaren-simple"))
  .settings(BuildConfig.commonSettings)
  .settings(
    Compile / mainClass := Some("com.hunorkovacs.itsaren.simple.ItsAren"),
    libraryDependencies ++= Dependencies.ItsarenSimple
  )

lazy val `itsaren` = (project in file("."))
  .aggregate(
    `itsaren-simple`
  )
