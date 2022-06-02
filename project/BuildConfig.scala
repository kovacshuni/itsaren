import sbt.Keys._
import sbt._

object BuildConfig {

  lazy val commonSettings = Seq(
    run / fork := true,
    organization := "com.hunorkovacs",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "3.1.2"
    // scalacOptions := Seq()
  )

}
