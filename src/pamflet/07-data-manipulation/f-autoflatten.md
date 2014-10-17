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