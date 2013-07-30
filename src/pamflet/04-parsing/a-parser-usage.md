Parser Usage
------------

Using a Morph parser to parse data is easy. For example, to parse a JSON file
called `data.json`, you can do:

```scala
val data = parse file "data.json" using JsonParser
```

Parsing a string is equally easy. For example, to parse the XML string
`<test>lorem impsum...</test>`, you can do:

```scala
val data = parse string "<test>lorem ipsum...</test>"
  using XmlParser
```

Parsers can also be called directly (instead of using the DSL that
is used in the examples above). All parsers have `apply` methods that take
either a string or an array of characters and return the root of the generated
abstract syntax tree. The second example from above can be rewritten as follows:

```scala
val data = XmlParser("<test>lorem ipsum...</test>")
```

Morph currently has built in parsers for the following data types:

    * JSON (JsonParser)
    * XML (XmlParser)
    * CSV (CsvParser)
