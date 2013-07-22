name := "morph"

version := "0.0.1-SNAPSHOT"

scalaVersion := "2.10.2"

javacOptions := Seq(
  "-Xlint:unchecked"
)

scalacOptions := Seq(
  "-unchecked", "-deprecation", "-feature", "-Xfatal-warnings"
)

resolvers ++= Seq(
  "clojars" at "http://clojars.org/repo",
  "clojure-releases" at "http://build.clojure.org/releases"
)

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.0",
  "org.scalatest" %% "scalatest" % "1.9.1" % "test",
  "org.parboiled" %% "parboiled-scala" % "1.1.5"
)

initialCommands in console := """
  |import morph.ast._
  |import morph.ast.Implicits._
  |import morph.ast.DSL._
  |import morph.parser._
  |import morph.parser.Interface._
  |import morph.utils.Utils._
  """.stripMargin
