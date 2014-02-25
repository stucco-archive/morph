package gov.ornl.stucco.morph.ast;

import org.scalatest.FunSuite

import gov.ornl.stucco.morph.ast._
import gov.ornl.stucco.morph.ast.Implicits._

class AstSuite extends FunSuite {

  test("convert empty object node to json") {
    val json = ObjectNode().toString
    assert(json === "{}")
  }

  test("convert empty array node to json") {
    val json = ArrayNode().toString
    assert(json === "[]")
  }

  test("convert object node to json") {
    val json = ObjectNode("test" -> TrueNode, "nullvalue" -> NullNode).toString
    assert(json ===
      """|{
         |  "test": true,
         |  "nullvalue": null
         |}""".stripMargin)
  }

  test("convert array node to json") {
    val json = ArrayNode(StringNode("a"), StringNode("test")).toString
    assert(json ===
      """|[
         |  "a",
         |  "test"
         |]""".stripMargin)
  }

  test("implicit string, boolean, and number conversion in array to json") {

    val json = ArrayNode(1, true, "test", 3.1415).toString
    assert(json ===
      """|[
         |  1,
         |  true,
         |  "test",
         |  3.1415
         |]""".stripMargin)
  }

  test("implicit (string, string) pair conversion in object to json") {

    val json = ObjectNode("key" -> "value").toString
    assert(json ===
      """|{
         |  "key": "value"
         |}""".stripMargin)
  }

  test("nested objects / arrays") {
	  

    val A = ArrayNode
    val O = ObjectNode
    val json = O("array" -> A(1, 2), "nested" -> A(A(true, A()))).toString
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
}
