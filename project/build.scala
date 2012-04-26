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
      "net.databinder" %% "dispatch-http" % "0.8.8",
      "org.specs2" %% "specs2" % "1.9" % "test",
      "org.slf4j" % "slf4j-api" % "1.6.1",
      "org.slf4j" % "slf4j-log4j12" % "1.6.1",
      "com.weiglewilczek.slf4s" %% "slf4s" % "1.0.7",
      "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.0",
      "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.0"
    ),
    resolvers ++= Seq(
      "snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
      "releases"  at "http://oss.sonatype.org/content/repositories/releases",
      "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
    ),
    scalacOptions += "-deprecation"
  )

  lazy val li2Latex = 
    Project("Li2Latex", file("."), settings = shared)
}