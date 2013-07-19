Option Usage
------------

DSL operations use Scala's `Option` types, which are containers for values that
may or may not exist (they can be thought of as a type-safe alternative to
using `null`). An option may be either a `Some` or a `None`, and inner values
can be extracted using pattern matching.

```scala
scala> val x: Option[ValueNode] = "options are useful"
x: Option[morph.ast.ValueNode] = Some("options are useful")

scala> x match {
     |   case Some(str) => println(str)
     |   case None      => // do nothing
     | }
"options are useful"
```

Options make it really easy to chain computations that may or may not be
successful, which results in a very clean DSL. For most transformations,
methods on `Option` are not necessary - just using DSL methods will suffice.
For more complex transformations, it may be especially useful to use the `map`,
`flatMap`, and `collect` methods, which are much more concise than using
pattern matching.

```scala
scala> val x: Option[Int] = None
x: Option[Int] = None

scala> x map { num => num + 1 }
res0: Option[Int] = None

scala> val y = Some(3)
y: Option[Int] = Some(3)

scala> y map { num => num + 1 }
res1: Option[Int] = Some(4)

scala> val z = Some(4)
z: Some[Int] = Some(4)

scala> z flatMap {
     |   case n if n % 2 == 0 => Some(n / 2)
     |   case _ => None
     | } map { n => n + 5 }
res2: Option[Int] = Some(7)

scala> val w = Some(6)
w: Some[Int] = Some(6)

scala> w collect {
     |   case n if n > 5 => n
     | }
res3: Option[Int] = Some(6)
```

As shown above, transformations on `None` simply result in `None`. This can
make expressing a series of computations very concise. The `*(...)` array
constructor and `^(...)` object constructor automatically filter out elements
and values that are `None`.

> For more information on `Option`, see the
[Scaladoc](http://www.scala-lang.org/api/current/index.html#scala.Option) or a
[quick reference](http://blog.tmorris.net/posts/scalaoption-cheat-sheet/).
