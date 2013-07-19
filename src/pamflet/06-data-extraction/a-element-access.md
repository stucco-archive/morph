Element Access
--------------

Accessing elements in arrays is done using the `get` method. Morph uses zero
based indexing.

```scala
scala> val arr = *("zero", "one", "two")
arr: morph.ast.ArrayNode =
[
  "zero",
  "one",
  "two"
]

scala> val second = arr get 1
second: Option[morph.ast.ValueNode] = Some("one")

scala> val tenth = arr get 9
tenth: Option[morph.ast.ValueNode] = None
```

Attempting to get an element by index on a non-array node will always return `None`.
