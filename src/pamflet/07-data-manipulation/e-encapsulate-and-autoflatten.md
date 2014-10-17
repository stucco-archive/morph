Encapsulate
---------------

The encapsulate method ensures that the contents are always contained in some outer array.
This is useful in cases where some value could otherwise be either a single item, or a list of items.  (This sometimes occures when processing XML data, for example.)
If the item to be 'encapsulated' is already a list, then it is left as-is (without adding an additional outer list.)

```scala
scala> val a = ^("a"->"aaa","b"->"bbb")
a: morph.ast.ObjectNode = 
{
  "a": "aaa",
  "b": "bbb"
}

scala> val a = ^("a"->"aaa","b"->"bbb").encapsulate
a: Option[morph.ast.ValueNode] = 
Some([
  {
    "a": "aaa",
    "b": "bbb"
  }
])

scala> val a = *(^("a"->"aaa","b"->"bbb"),^("c"->"ccc")).encapsulate
a: Option[morph.ast.ValueNode] = 
Some([
  {
    "a": "aaa",
    "b": "bbb"
  },
  {
    "c": "ccc"
  }
])


```

Auto Flatten
---------------

Auto Flatten takes a (potentially) two-dimentional array, and 'flattens' it into a single array.

```scala
scala> val a = Some(*(*(1,2,3),4))
a: Some[morph.ast.ArrayNode] = 
Some([
  [
    1,
    2,
    3
  ],
  4
])

scala> val a = *(*(1,2,3),4).autoFlatten
a: Option[morph.ast.ValueNode] = 
Some([
  1,
  2,
  3,
  4
])

scala> val arr = *(1,2,3).autoFlatten
arr: Option[morph.ast.ValueNode] = 
Some([
  1,
  2,
  3
])
```

Note that this reduces the nesting by at most one level.  If desired, it can be invoked repeatedly to remove additional nesting.

```scala
scala> val a = Some(*(1, None, *(*(1,2,3))))
a: Some[morph.ast.ArrayNode] = 
Some([
  1,
  [
    [
      1,
      2,
      3
    ]
  ]
])

scala> val a = Some(*(1, None, *(*(1,2,3)))).autoFlatten
a: Option[morph.ast.ValueNode] = 
Some([
  1,
  [
    1,
    2,
    3
  ]
])

scala> val a = Some(*(1, None, *(*(1,2,3)))).autoFlatten.autoFlatten
a: Option[morph.ast.ValueNode] = 
Some([
  1,
  1,
  2,
  3
])

```