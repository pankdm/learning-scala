name := "icfpc-2007"

version := "0.1"

scalaVersion := "2.10.1"

scalacOptions ++= Seq("-deprecation", "-feature")

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.1" % "test"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.1"

unmanagedBase <<= baseDirectory { base => base / "custom_lib" }