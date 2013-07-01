import org.scalatest.FunSuite

import converter.ast._

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
    val json = ArrayNode(StringNode("a"), StringNode("test")).toJson
    assert(json ===
      """|[
         |  "a",
         |  "test"
         |]""".stripMargin)
  }

  test("implicit string, boolean, and number conversion in array to json") {
    import converter.ast.Implicits._

    val json = ArrayNode(1, true, "test", 3.1415).toJson
    assert(json ===
      """|[
         |  1,
         |  true,
         |  "test",
         |  3.1415
         |]""".stripMargin)
  }

  test("implicit (string, string) pair conversion in object to json") {
    import converter.ast.Implicits._

    val json = ObjectNode("key" -> "value").toJson
    assert(json ===
      """|{
         |  "key": "value"
         |}""".stripMargin)
  }

  test("nested objects / arrays") {
    import converter.ast.Implicits._

    val A = ArrayNode
    val O = ObjectNode
    val json = O("array" -> A(1, 2), "nested" -> A(A(true, A()))).toJson
    assert(json ===
      """|{
         |  "array": [
         |    1,
         |    2
         |  ],
         |  "nested": [
         |    [
         |      true,
         |      []
         |    ]
         |  ]
         |}""".stripMargin)

  }

  test("invalid json conversion throws exception") {
    intercept[UnsupportedOperationException] {
      NullNode.toJson
    }

    intercept[UnsupportedOperationException] {
      TrueNode.toJson
    }

    intercept[UnsupportedOperationException] {
      FalseNode.toJson
    }

    intercept[UnsupportedOperationException] {
      StringNode("test").toJson
    }
  }
}
