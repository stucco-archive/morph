package gov.ornl.stucco.morph.utils;

import org.scalatest.FunSuite

import gov.ornl.stucco.morph.utils.Utils._

class UtilsSuite extends FunSuite {

  test("indent single line string") {
    assert("this is a test".indent === "  this is a test")
  }

  test("indent multi line string") {
    assert(
      """|this is
         |a test""".stripMargin.indent
        ===
        "  this is\n  a test"
    )
  }

  test("indent single line string by specified amount") {
    assert("this is a test".indent(4) === "    this is a test")
  }

  test("indent multi line string by specified amount") {
    assert(
      """|this is
         |a test""".stripMargin.indent(4)
        ===
        "    this is\n    a test"
    )
  }

  test("escape a string with a newline") {
    assert(
      """|one
         |two""".stripMargin.escaped
        ===
        "one\\ntwo"
    )
  }

  test("escape a string with various escape sequences") {
    assert("\"\\\b\n\r".escaped === "\\\"\\\\\\b\\n\\r")
  }
}
