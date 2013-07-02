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
    assert(library \ "name" === S("Test Library"))

    assert(library \ "books" \ "one" === O("author" -> "nobody"))
  }

  test("safely search for key in object") {
    assert(library \? "name" === Some(S("Test Library")))

    assert(library \? "books" \? "one" === Some(O("author" -> "nobody")))
  }

  test("search for nonexistant key in object throws exception") {
    intercept[NoSuchElementException] {
      library \ "bla"
    }
  }

  test("recursively search for key") {
    assert(library \\ "author" === A("nobody", "somebody", "anybody"))

    assert(program \ "menu" \ "popup" \\ "value" === A("New", "Open", "Close"))

    assert(program \\ "value" === A("File", "New", "Open", "Close"))
  }

  test("find nodes by predicate") {
    val matched = program find {
      case S(str) => str contains "()"
      case _ => false
    }
    assert(matched === A("CreateNewDoc()", "OpenDoc()", "CloseDoc()"))
  }
}
