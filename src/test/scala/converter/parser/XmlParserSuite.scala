import org.scalatest.FunSuite

import converter.parser.{XmlParser, ParsingException}
import converter.ast._
import converter.ast.Implicits._

class XmlParserSuite extends FunSuite {

  // to make testing easier
  val O = ObjectNode
  val A = ArrayNode
  val S = StringNode
  val N = NumberNode
  val X = XmlParser

  test("parse single element empty xml") {
    assert(X("<a></a>") === O("a" -> O()))
  }

  test("parse nested xml") {
    val xml = "<a><b></b></a>"
    assert(X(xml) === O("a" -> O("b" -> O())))
  }

  test("parse xml with attributes") {
    val xml = "<test attr='with array'></test>"
    assert(X(xml) === O("test" -> O("@attr" -> "with array")))
}

  test("parse xml with content") {
    val xml = "<test>Content here</test>"
    assert(X(xml) === O("test" -> O("#content" -> "Content here")))
  }

  test("parse xml array") {
    val xml = "<test><array num='1'/><array num='2'/></test>"
    assert(X(xml) ===
      O("test" -> O("arrays" -> A(O("@num" -> "1"), O("@num" -> "2")))))
  }

  test("invalid xml throws exception") {
    intercept[ParsingException] {
      X("<tag></ttag>")
    }
  }
}
