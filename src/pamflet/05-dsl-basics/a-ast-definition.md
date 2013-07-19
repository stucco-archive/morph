AST Definition
--------------

Morph uses an abstract syntax tree structure that maps almost one-to-one with
JSON for simplicity. Below is a diagram of the class hierarchy for AST node
types.

<!-- scala syntax highlighting seems to work well here -->

```scala
ValueNode         [sealed abstract class]
|
+-> ObjectNode    [case class + object]
|
+-> ArrayNode     [case class + object]
|
+-> StringNode    [case class + object]
|
+-> NumberNode    [case class + object]
|
+-> BooleanNode   [sealed abstract class + object]
|   |
|   +-> TrueNode  [case object]
|   |
|   +-> FalseNode [case object]
|
+-> NullNode      [case object]
```
