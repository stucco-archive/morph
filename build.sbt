name := "converter"

version := "0.0.1"

scalaVersion := "2.10.1"

scalacOptions := Seq("-unchecked", "-deprecation", "-feature")

resolvers ++= Seq(
  "clojars" at "http://clojars.org/repo",
  "clojure-releases" at "http://build.clojure.com/releases"
)

libraryDependencies ++= Seq(
  // "org.scalaz" %% "scalaz-core" % "7.0.0",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "org.parboiled" %% "parboiled-scala" % "1.1.5"
)
