AST Node Creation
-----------------

Morph includes companion objects for most data types and defines several
implicit conversions that make manual AST node construction easy. This makes
type ascription to AST node types work properly (through implicit conversion).

```scala
scala> val num: NumberNode= 3
num: morph.ast.NumberNode = 3

scala> val cond: ValueNode = true
cond: morph.ast.ValueNode = true
```

Implicit conversions can be especially handy when creating nested nodes.

```scala
scala> val arr = ArrayNode(1, 2, "test", true)
arr: morph.ast.ArrayNode =
[
  1,
  2,
  "test",
  true
]
```

There is a shorthand syntax for creating an `ArrayNode` or `ObjectNode`.
For example, an array can be created like `*(1, "two", false)`, and an object
can be created like `^("a" -> true, "b" -> "c", "d" -> *(true, false))`.

Using this syntax has an additional benefit - you can mix normal `ValueNode`
types and `Option[ValueNode]` types, and the `Option[ValueNode]` data that
have the value `None` are automatically filtered out.

```scala
scala> val person = ^(
     |   "name" -> "John Smith",
     |   "pets" -> None,
     |   "age" -> 35,
     |   "children" -> Some(*("Jane", "Robert"))
     | )
person: morph.ast.ObjectNode =
{
  "name": "John Smith",
  "age": 35,
  "children": [
    "Jane",
    "Robert"
  ]
}
```

Thanks to implicit conversions, programs written in the Morph DSL can be made
extremely concise, and they usually don't need type annotations at all. Programs
can look like they are written in a dynamic language, but are still statically
typed (with full type-safety).
