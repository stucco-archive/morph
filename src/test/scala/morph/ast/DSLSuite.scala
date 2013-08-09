import org.scalatest.FunSuite

import morph.ast._
import morph.ast.Implicits._
import morph.ast.DSL._

import morph.parser.JsonParser

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
    assert(A("one", "two", "three", "four") ~> 1 === Some(S("two")))
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
    val mapped = program ~> "menu" ~> "popup" ~> "menuitem" %-> { x =>
      x ~> "value"
    }
    assert(mapped === Some(A("New", "Open", "Close")))
  }

  test("map partial function over array") {
    val mapped = program ~>> "value" %~> {
      case StringNode(str) if str.length == 3 => str.reverse
    }
    assert(mapped === Some(A("weN")))
  }

  test("apply or map function and partial") {
    val mapped = *(1, 2, 3) %%-> {
      _ %%~> {
        case NumberNode(value) if value < 3 => value * 2
      }
    }
    assert(mapped === Some(A(2, 4)))
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
    val trans = A(1, 2, 3, "hello") %%~> {
      case NumberNode(n) => n * 2
    }
    assert(trans === Some(A(2, 4, 6)))
  }

  test("apply a filter") {
    val filtered = A(1, 2, "hello", true) applyFilter { _.isNumber }
    assert(filtered === Some(A(1, 2)))
  }

  test("flatten an array") {
    val flattened = A(A(1, 2), A(3), A(), A(4, 5, 6)).applyFlatten
    assert(flattened === Some(A(1, 2, 3, 4, 5, 6)))
  }

  test("automatically flatten an array of elements and arrays") {
    val flattened = A(A(1, 2), 3, A(4), A()).autoFlatten
    assert(flattened === Some(A(1, 2, 3, 4)))
  }

  test("recursively flatten an array") {
    val flattened = A(1, A(2, A(3)), A(A(4)), A(5), A(A())).recFlatten
    assert(flattened === Some(A(1, 2, 3, 4, 5)))
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
    val trans = program ~> "menu" ~> "popup" ~> "menuitem" %%-> { x =>
      ^("val" -> { x ~> "value" })
    }
    assert(trans === Some(A(O("val" -> S("New")), O("val" -> S("Open")),
      O("val" -> S("Close")))))
  }

  test("safely block") {
    val mapped = A(1, 2, "str") mapFunc { node =>
      Safely {
        node.asNumber * 2
      }
    }
    assert(mapped === Some(A(2, 4)))
  }
}
