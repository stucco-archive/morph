Type Conversion
---------------

Usually, Morph DSL methods are enough to perform transformations on data. When
you need to perform very complex transformations, it may be necessary to access
Scala's built-in data types to take advantage of all the methods defined on
them. Fortunately, it is easy to access these underlying data structures.

Morph defines a family of methods, `is{something}` and `as{something}`.
However, when performing an improper conversion (e.g. `"hello".asNumber`), an
exception may be thrown. One easy way of dealing with this is wrapping all
operations of this kind in a `Safely { ... }` block, which will return `None`
if an operation cannot be performed.

Consider the following data:

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

Suppose we want to extract data from these references in the following way. If
a URL is available, extract the URL, otherwise concatenate the source and text.

```scala
scala> val extracted = refs mapFunc { ref =>
     |   ref get "@url" orElse Safely {
     |     (ref get "@source").asString + ": " +
     |     (ref get "#text").asString
     |   }
     | }
extracted: Option[morph.ast.ValueNode] =
Some([
  "CERT: CA-98.12.mountd",
  "www.ciac.org/...",
  "XF: linux-mountd-bo"
])
```
