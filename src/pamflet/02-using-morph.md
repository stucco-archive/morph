Using Morph
-----------

Morph is split up into several modules. Generally, it is a good practice to
import only the classes and traits that you need.

When learning Morph, it is incredibly helpful to test things out using the
Scala REPL, which can be started using `sbt console`. When using the REPL, it
is a good idea to import all Morph modules for ease of use. This can be
accomplished by running the following code.

```scala
import morph.ast._
import morph.ast.Implicits._
import morph.ast.DSL._
import morph.parser._
import morph.parser.Interface._
import morph.extractor.Interface._
import morph.extractor.utils.Utils._
```

This process can be automated by adding the following to `build.sbt`.

```scala
initialCommands in console := """
  |import morph.ast._
  |import morph.ast.Implicits._
  |import morph.ast.DSL._
  |import morph.parser._
  |import morph.parser.Interface._
  |import morph.extractor.Interface._
  |import morph.utils.Utils._
  """.stripMargin
```
