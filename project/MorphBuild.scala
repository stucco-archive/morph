import sbt._
import Keys._

object MorphBuild extends Build {
  lazy val root = Project("morph", file("."))
    .dependsOn(jsonjava)

  lazy val jsonjava = GitHub("anishathalye", "JSON-java", "morph")

  def GitHub(user: String, project: String, tag: String = null) =
      RootProject(
            uri("https://github.com/%s/%s.git#%s".format(user, project, tag))
          )
}
