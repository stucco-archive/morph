Pretty Printing
---------------

All AST node types' `toString` methods are overloaded so that nodes can be
pretty printed as JSON. The string representation is 100% compliant with the
JSON spec (special characters in strings are escaped properly, and so on).

This easy-to-read visual representation can be especially handy when debugging
Morph DSL code.

```scala
scala> val obj = ObjectNode(
     |   "pretty_printing_enabled" -> TrueNode,
     |   "mode" -> StringNode("JSON"),
     |   "settings" -> ArrayNode(
     |     StringNode("spaces_over_tabs"),
     |     StringNode("vim_over_emacs")
     |   )
     | )
obj: morph.ast.ObjectNode =
{
  "pretty_printing_enabled": true,
  "mode": "JSON",
  "settings": [
    "spaces_over_tabs",
    "vim_over_emacs"
  ]
}
```
