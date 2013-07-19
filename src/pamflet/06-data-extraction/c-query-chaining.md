Query Chaining
--------------

Queries on AST node types can be chained in arbitrarily complex ways.

For example, consider the following `data.xml` file.

```xml
<?xml version="1.0"?>
<item type="CVE" name="CVE-1999-0002" seq="1999-0002">
  <status>Entry</status>
  <desc>Buffer overflow in NFS mountd...</desc>
  <refs>
    <ref source="CERT">CA-98.12.mountd</ref>
    <ref source="CIAC" url="www.ciac.org/...">J-006</ref>
    <ref source="XF">linux-mountd-bo</ref>
  </refs>
</item>
```

First, the file must be parsed.

```scala
scala> val data = parse file "data.xml" using XmlParser
data: morph.ast.ValueNode =
{
  "item": {
    "@name": "CVE-1999-0002",
    "desc": "Buffer overflow in NFS mountd...",
    "refs": {
      "ref": [
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
    },
    "status": "Entry",
    "@type": "CVE",
    "@seq": "1999-0002"
  }
}
```

Elements can easily be extracted from the data by chaining operations.

```scala
scala> val name = data get "item" get "@name"
name: Option[morph.ast.ValueNode] = Some("CVE-1999-0002")

scala> val nonexistant = data get "nonexistant" get "morenonexistant" get 3
nonexistant: Option[morph.ast.ValueNode] = None

scala> val ref1 = data get "item" get "refs" get "ref" get 1
ref1: Option[morph.ast.ValueNode] =
Some({
  "@source": "CERT",
  "#text": "CA-98.12.mountd"
})
```
