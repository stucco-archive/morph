Build Dependency
----------------

Configuring Morph as a build dependency is really simple if you're using
[sbt](http://www.scala-sbt.org/).

To add a GitHub build dependency, you need to use a .scala build
definition. This type of build definitions is more powerful than the simpler
.sbt build definition, and the two types of files can be used together to
easily create a clean project definition.

.scala build definitions are saved in the `project` folder within your project
root directory. Build definition files are named with the convention
`{ProjectName}Build.scala`.

Here is an example .scala build definition for a project named MyProject. The
tag used in the example is `master` (which is actually a branch name), but you
should use a tagged release version to fix Morph at a specific build. The
rest of the settings can remain in the `build.sbt` file in the project root
directory - those settings are included when sbt is run.

```scala
import sbt._
import Keys._

object MyProjectBuild extends Build {
  lazy val root = Project("MyProject", file("."))
    .dependsOn(morph)

  lazy val morph = GitHub("stucco", "morph", "master")

  def GitHub(user: String, project: String, tag: String) =
    RootProject(
      uri("https://github.com/%s/%s.git#%s".format(user, project, tag))
    )
}
```
