import org.scalatest.FunSuite

import converter.ast._
import converter.ast.Implicits._

class AstSuite extends FunSuite {

  test("convert empty object node to json") {
    val json = ObjectNode().toJson
    assert(json === "{}")
  }

  test("convert empty array node to json") {
    val json = ArrayNode().toJson
    assert(json === "[]")
  }

  test("convert object node to json") {
    val json = ObjectNode("test" -> TrueNode, "nullvalue" -> NullNode).toJson
    assert(json ===
      """|{
         |  "test": true,
         |  "nullvalue": null
         |}""".stripMargin)
  }

  test("convert array node to json") {
    val json = ArrayNode("this", "is", "a", "test").toJson
    assert(json ===
      """|[
         |  "this",
         |  "is",
         |  "a",
         |  "test"
         |]""".stripMargin)
  }

  test("implicit string, boolean, and number conversion in array to json") {
    val json = ArrayNode(1, true, "test").toJson
    assert(json ===
      """|[
         |  1,
         |  true,
         |  "test"
         |]""".stripMargin)
  }

  test("invalid json conversion throws exception") {
    intercept[UnsupportedOperationException] {
      TrueNode.toJson
    }
  }
}
