import org.scalatest.FunSuite

import morph.parser.{ JsonParser, ParsingException }
import morph.ast._
import morph.ast.Implicits._

class JsonParserSuite extends FunSuite {

  // to make testing easier
  val O = ObjectNode
  val A = ArrayNode
  val S = StringNode
  val N = NumberNode
  val J = JsonParser

  test("parse single string of json") {
    assert(J(" \"test\"  ") === StringNode("test"))
  }

  test("parse json array") {
    val json = "[true, false, null, \"hello\"]"
    assert(J(json) === A(true, false, NullNode, "hello"))
  }

  test("parse json number") {
    assert(J("3.14159") === N("3.14159"))

    assert(J("1e10") === N("10000000000"))

    assert(J("1.2e-1") === N("0.12"))
  }

  test("parse complex json object") {
    val json = """|{
                  |  "test": true,
                  |  "string": "bla",
                  |  "array": [
                  |    1,
                  |    2
                  |  ],
                  |  "object": {
                  |    "key": "value"
                  |  }
                  |}""".stripMargin
    val obj = O("test" -> true, "string" -> "bla", "array" -> A(1, 2),
      "object" -> O("key" -> S("value")))

    assert(J(json) === obj)
  }

  test("invalid json throws exception") {
    intercept[ParsingException] {
      J("{\"key\": string without quotes")
    }
  }
}
