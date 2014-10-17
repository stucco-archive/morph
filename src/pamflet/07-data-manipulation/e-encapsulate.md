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