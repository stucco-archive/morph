Partial Function Mapping
------------------------

It is possible to map partial functions (functions that aren't defined for all
possible inputs, usually constructed in Scala using pattern matching) over
arrays.

Consider the following data:

```scala
scala> val data = *("text", 22, 400, true, NullNode, -5.3, false, 1.2e4)
data: morph.ast.ArrayNode =
[
  "text",
  22,
  400,
  true,
  null,
  -5.3,
  false,
  12000.0
]
```

Suppose we want to extract the numbers that are greater than 100, and then
return those numbers doubled. Here is one way we can do it:

```scala
scala> val doubled = data applyFilter { node =>
     |   node.isNumber && node.asNumber > 100
     | } mapFunc {
     |   _.asNumber * 2
     | }
doubled: Option[morph.ast.ValueNode] =
Some([
  800,
  24000.0
])
```

This is easier (and clearer) to do using a partial function defined on numbers
greater than 100:

```scala
scala> val doubled = data mapPartial {
     |   case NumberNode(value) if value > 100 => value * 2
     | }
doubled: Option[morph.ast.ValueNode] =
Some([
  800,
  24000.0
])
```
