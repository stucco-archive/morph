DSL Quick Reference
-------------------

Here is a list of all DSL methods, their symbolic aliases, and short
descriptions.

---

###### `get  ==  ~>`

Gets an element in an `ArrayNode` by index or a value in an `ObjectNode` by key.

---

###### `recGet  ==  ~>>`

Recursively searches for a value corresponding to the specified key.

---

###### `mapFunc == %->`

Maps a function over an array node.

---

###### `mapPartial == %~>`

Maps a partial function over an array node.

---

###### `applyOrMapFunc == %%->`

Applies a function to a value node or maps the function over the elements of an
array node.

---

###### `applyOrMapPartial == %%~>`

Applies a partial function to a value node or maps the partial function over
the elements of an array node.

---

###### `applyFilter`

Filters an array node by a predicate.

---

This list is only a quick reference. Certain methods in particular, the
`is{something}` and `as{something}` family of methods as well as `Safely` are
not covered. Please refer to the [Scaladoc]($scaladocurl$) for more details.
