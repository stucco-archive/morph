DSL Basics
----------

The Morph DSL is a concise but robust way of describing transformations on
structured data.

The DSL can be used in two different ways. You can import the necessary objects
and use them as you please, but this is usually unnecessarily complex. The
easier way is to subclass `morph.extractor.Extractor` and implement the one
required method that performs a transformation on a node.

```scala
def extract(node: ValueNode): ValueNode
```

For example, here is an extractor that recursively extracts values with the
name "data".

```scala
import morph.extractor.Extractor

object ExampleExtractor extends Extractor {
  def extract(node: ValueNode): ValueNode = {
    node recGet "data"
  }
}
```
