package gov.ornl.stucco.morph.parser;

import org.scalatest.FunSuite

import gov.ornl.stucco.morph.parser._
import gov.ornl.stucco.morph.ast._
import gov.ornl.stucco.morph.ast.Implicits._

class XmlParserSuite extends FunSuite {

  // to make testing easier
  val O = ObjectNode
  val A = ArrayNode
  val S = StringNode
  val N = NumberNode
  val X = XmlParser

  test("parse single element empty xml") {
    assert(X("<a></a>") === O("a" -> NullNode))
  }

  test("parse self closing xml tag") {
    assert(X("<tag/>") === O("tag" -> NullNode))
  }

  test("parse nested xml") {
    val xml = "<a><b></b></a>"
    assert(X(xml) === O("a" -> O("b" -> NullNode)))
  }

  test("parse xml with attributes") {
    val xml = "<test attr='with array'></test>"
    assert(X(xml) === O("test" -> O("@attr" -> "with array")))
  }

  test("parse xml with content") {
    val xml = "<test>Content here</test>"
    assert(X(xml) === O("test" -> "Content here"))
  }

  test("parse xml array") {
    val xml = "<test><array num='1'/><array num='2'/></test>"
    assert(X(xml) ===
      O("test" -> O("array" -> A(O("@num" -> N(1)), O("@num" -> N(2))))))
  }

  test("invalid xml throws exception") {
    intercept[ParsingException] {
      X("<tag></ttag>")
    }
  }
}
