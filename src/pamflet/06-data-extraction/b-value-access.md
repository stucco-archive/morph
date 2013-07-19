Value Access
------------

Accessing fields in objects is done using the `get` method.

```scala
scala> val obj = ^("bool" -> true,
     |             "str" -> "test string"
     |            )
obj: morph.ast.ObjectNode =
{
  "bool": true,
  "str": "test string"
}

scala> val bool = obj get "bool"
bool: Option[morph.ast.ValueNode] = Some(true)

scala> val nonexistant = obj get "nonexistant"
nonexistant: Option[morph.ast.ValueNode] = None
```

Attempting to get a value by key on a non-object node will always return `None`.
