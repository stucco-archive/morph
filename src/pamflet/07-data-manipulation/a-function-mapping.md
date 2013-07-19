Function Mapping
----------------

One common way to transform data is mapping functions over arrays.

For example, consider the following data:

```scala
scala> val refs = *(
     |   ^("@source" -> "CERT", "#text" -> "CA-98.12.mountd"),
     |   ^("@source" -> "CIAC", "@url" -> "www.ciac.org/...", "#text" -> "J-006"),
     |   ^("@source" -> "XF", "#text" -> "linux-mountd-bo")
     | )
refs: morph.ast.ArrayNode =
[
  {
    "@source": "CERT",
    "#text": "CA-98.12.mountd"
  },
  {
    "@source": "CIAC",
    "@url": "www.ciac.org/...",
    "#text": "J-006"
  },
  {
    "@source": "XF",
    "#text": "linux-mountd-bo"
  }
]
```

We can extract the sources as follows:

```scala
scala> val sources = refs mapFunc {
     |   ref => ref get "@source"
     | }
sources: Option[morph.ast.ValueNode] =
Some([
  "CERT",
  "CIAC",
  "XF"
])
```

We can extract URLs in an identical manner. Queries to extract URLs that are unsuccessful are automatically filtered out.

```scala
scala> val urls = refs mapFunc {
     |   ref => ref get "@url"
     | }
urls: Option[morph.ast.ValueNode] =
Some([
  "www.ciac.org/..."
])
```
