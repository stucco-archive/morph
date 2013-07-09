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
    assert(library ~> "name" === Some(S("Test Library")))

    assert(library ~> "books" ~> "one" === Some(O("author" -> "nobody")))
  }

  test("get element by index in array") {
    assert(A("one", "two", "three", "four") ~> 2 === Some(S("two")))
  }

  test("search for nonexistant key in object returns none") {
    assert(library ~> "bla" === None)
  }

  test("recursively search for key") {
    assert(library ~>> "author" === Some(A("nobody", "somebody", "anybody")))

    assert(program ~> "menu" ~> "popup" ~>> "value" ===
      Some(A("New", "Open", "Close")))

    assert(program ~>> "value" === Some(A("File", "New", "Open", "Close")))
  }

  test("map function over array") {
    val mapped = program ~> "menu" ~> "popup" ~> "menuitem" %~> { x =>
      x ~> "value"
    }
    assert(mapped === Some(A("New", "Open", "Close")))
  }

  test("object constructor") {
    val obj = ^("a" -> Some(S("test")), "b" -> None, "c" -> TrueNode)
    assert(obj === O("a" -> "test", "c" -> TrueNode))
  }

  test("array constructor") {
    val arr = *("a", Some(NullNode), None)
    assert(arr === A("a", NullNode))
  }

  test("apply function to transform a node") {
    val trans = library ~> "name" map { *(_) }
    assert(trans === Some(A("Test Library")))
  }

  test("apply a partial function that may be mapped to transform a node") {
    val trans = A(1, 2, 3, "hello") %-> {
      case NumberNode(n) => n * 2
    }
    assert(trans === Some(A(2, 4, 6)))
  }

  test("nodeEmpty and nodeNonEmpty functionality") {
    assert(O().nodeEmpty)
    assert(O("a" -> "test").nodeNonEmpty)
    assert(A().nodeEmpty)
    assert(A("test").nodeNonEmpty)
    assert(NullNode.nodeEmpty)
    assert(TrueNode.nodeNonEmpty)
  }

  test("complex manipulation involving applyOrMap and Options") {
    val trans = program ~> "menu" ~> "popup" ~> "menuitem" %%~> { x =>
      ^("val" -> { x ~> "value" })
    }
    assert(trans === Some(A(O("val" -> S("New")), O("val" -> S("Open")),
      O("val" -> S("Close")))))
  }
}
