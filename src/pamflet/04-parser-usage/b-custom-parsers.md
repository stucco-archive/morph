Custom Parsers
--------------

Morph supports the use of custom parsers. Parsers should throw a
`morph.parser.ParsingException` if they encounter an error while
parsing. All custom parsers must implement the `AstBuilder` trait:

```scala
trait AstBuilder {
  def apply(input: String): ValueNode
  def apply(input: Array[Char]): ValueNode
}
```

If using [parboiled](http://parboiled.org/), it is easier to
subclass `morph.parser.BaseParser` and implement the root rule:

```scala
def RootRule: Rule1[ValueNode]
```

It is recommended that custom parsers are written in Scala (it's much easier),
but it is also possible to write parsers in Java. These parsers must implement
`AstBuilder` as well:

```java
interface AstBuilder {
    ValueNode apply(String input);
    ValueNode apply(char[] input);
}
```
