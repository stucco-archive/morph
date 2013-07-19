Auto Collapsing
---------------

When performing complex queries, some may or may not be successful. This is
indicated by the return type of methods (`Option[ValueNode]`). When
constructing a final object or array (that is meant to be used for further
processing or output), unsuccessful queries should be filtered out. This is
done automatically by the `*(...)` array constructor and the `^(...)` object
constructor.

```scala
scala> val arr = *(1, 2, None, 3)
arr: morph.ast.ArrayNode =
[
  1,
  2,
  3
]

scala> val obj = ^(
     |   "test" -> true,
     |   "unsuccessful" -> None
     | )
obj: morph.ast.ObjectNode =
{
  "test": true
}
```
