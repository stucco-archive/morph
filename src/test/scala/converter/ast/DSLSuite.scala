import org.scalatest.FunSuite

import converter.ast._
import converter.ast.Implicits._
import converter.ast.DSL._

import converter.parser.JsonParser

class DSLSuite extends FunSuite {

  // to make testing easier
  val O = ObjectNode
  val A = ArrayNode
  val S = StringNode
  val N = NumberNode

  val library = JsonParser("""
    {
      "name": "Test Library",
      "books": {
        "one": {
          "author": "nobody"
        },
        "two": {
          "author": "somebody"
        },
        "three": {
          "author": "anybody"
        }
      }
    }
  """)

  val program = JsonParser("""
    {"menu": {
      "id": "file",
      "value": "File",
      "popup": {
        "menuitem": [
          {"value": "New", "onclick": "CreateNewDoc()"},
          {"value": "Open", "onclick": "OpenDoc()"},
          {"value": "Close", "onclick": "CloseDoc()"}
        ]
      }
    }}
  """)

  test("search for key in object") {
    assert(library -> "name" === S("Test Library"))

    assert(library -> "books" -> "one" === O("author" -> "nobody"))
  }

  test("get element by index in array") {
    assert(A("one", "two", "three", "four") -> 2 === S("three")) // zero based indexing
  }

  test("safely search for key in object") {
    assert(library ~> "name" === Some(S("Test Library")))

    assert(library ~> "books" ~> "one" === Some(O("author" -> "nobody")))
  }

  test("search for nonexistant key in object throws exception") {
    intercept[NoSuchElementException] {
      library -> "bla"
    }
  }

  test("recursively search for key") {
    assert(library ->> "author" === A("nobody", "somebody", "anybody"))

    assert(program -> "menu" -> "popup" ->> "value" === A("New", "Open", "Close"))

    assert(program ->> "value" === A("File", "New", "Open", "Close"))
  }

  test("find nodes by predicate") {
    val matched = program find {
      case S(str) => str contains "()"
      case _ => false
    }
    assert(matched === A("CreateNewDoc()", "OpenDoc()", "CloseDoc()"))
  }

  test("filter nodes by predicate") {
    val filtered = program -> "menu" -> "popup" -> "menuitem" filter {
      _ -> "value" == S("New")
    }
    assert(filtered === A(O("value" -> "New", "onclick" -> "CreateNewDoc()")))
  }

  test("map function over array") {
    val mapped = program -> "menu" -> "popup" -> "menuitem" map {
      _ -> "value"
    }
    assert(mapped === A("New", "Open", "Close"))
  }

  test("object constructor") {
    val obj = ^("a" -> Some(S("test")), "b" -> None, "c" -> TrueNode)
    assert(obj === O("a" -> "test", "c" -> TrueNode))
  }

  test("array constructor") {
    val arr = *(S("a"), Some(NullNode), None)
    assert(arr === A("a", NullNode))
  }

  test("apply function to transform a node") {
    val trans = library -> "name" |> { *(_) }
    assert(trans === A("Test Library"))
  }

  test("apply function that returns an option to transform a node") {
    val trans = *(
      library |>~ { _ ~> "nonexistant" },
      library |>~ { _ ~> "name" }
    )
    assert(trans === A("Test Library"))
  }

  test("apply function that may be mapped to transform a node") {
    val trans = A(1, 2, 3) |+> {
      case NumberNode(n) => n * 2
      case _ => NullNode
    }
    assert(trans === A(2, 4, 6))
  }

  test("get the elements of an array node") {
    assert(A(1, 2, 3).elements === List(N(1), N(2), N(3)))
  }

  test("get the fields of an object node") {
    assert(O("a" -> "b", "c" -> 3).fields === Map("a" -> S("b"), "c" -> N(3)))
  }

  test("isEmpty and nonEmpty functionality") {
    assert(O().isEmpty)
    assert(O("a" -> "test").nonEmpty)
    assert(A().isEmpty)
    assert(A("test").nonEmpty)
    assert(NullNode.isEmpty)
    assert(TrueNode.nonEmpty)
  }

  test("complex manipulation involving applyOrMap and Options") {
    val trans = program ~> "menu" ~> "popup" ~> "menuitem" |+> { x =>
      ^("val" -> { x -> "value" })
    }
    assert(trans === Some(A(O("val" -> S("New")), O("val" -> S("Open")), O("val" -> S("Close")))))
  }
}
