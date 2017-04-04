import sbt.Keys._
import sbt._

object BuildConfig {

  lazy val commonSettings = Seq(
    run / fork := true,
    organization := "com.hunorkovacs",
    version := "1.0.0-SNAPSHOT",
    scalacOptions := Seq(
      "-encoding",
      "UTF-8",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-language:postfixOps",
      "-unchecked",
      "-deprecation",
      "-feature",
      "-Ywarn-unused-import"
    ),
    javaOptions in run += "-XX:+CMSClassUnloadingEnabled"
  )
}
