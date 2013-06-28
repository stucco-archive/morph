import org.scalatest.FunSuite

import converter.utils.Utils._

class UtilsSuite extends FunSuite {

  test("indent single line string") {
    assert("this is a test".indent === "  this is a test")
  }

  test("indent multi line string") {
    assert(
      """|this is
         |a test""".stripMargin.indent
         ===
      """|  this is
         |  a test""".stripMargin)
  }

  test("indent single line string by specified amount") {
    assert("this is a test".indent(4) === "    this is a test")
  }

  test("indent multi line string by specified amount") {
    assert(
      """|this is
         |a test""".stripMargin.indent(4)
         ===
      """|    this is
         |    a test""".stripMargin)
  }

  test("escape a string with a newline") {
    assert(
      """|one
         |two""".stripMargin.escaped
      ===
      "one\\ntwo")
  }
}
