import sbt._
import Keys._

object Li2Latex extends Build {
  val shared = Defaults.defaultSettings ++ Seq(
    name := "LinkedIn profile to Latex resume",
    organization := "com.ximyu",
    version := "0.1",
    scalaVersion := "2.9.1",
    libraryDependencies ++= Seq(
      "org.scribe" % "scribe" % "1.3.0",
      "net.databinder" %% "dispatch-http" % "0.8.8"
    )
  )

  lazy val li2Latex = 
    Project("Li2Latex", file("."), settings = shared)
}
