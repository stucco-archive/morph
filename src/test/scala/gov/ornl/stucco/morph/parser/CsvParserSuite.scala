package gov.ornl.stucco.morph.parser;

import org.scalatest.FunSuite

import gov.ornl.stucco.morph.parser._
import gov.ornl.stucco.morph.ast._
import gov.ornl.stucco.morph.ast.Implicits._

class CsvParserSuite extends FunSuite {

  // to make testing easier
  val A = ArrayNode
  val C = CsvParser

  test("parse single element csv with no quotes") {
    val csv = "test"
    val ast = A(A("test"))
    assert(C(csv) === ast)
  }

  test("parse multi element csv with no quotes") {
    val csv = "test,more test"
    val ast = A(A("test", "more test"))
    assert(C(csv) === ast)
  }

  test("parse single element csv surrounded by quotes") {
    val csv = "\"test\""
    val ast = A(A("test"))
    assert(C(csv) === ast)
  }

  test("parse multi element csv with spaces") {
    val csv = " I , like , spaces "
    val ast = A(A(" I ", " like ", " spaces "))
    assert(C(csv) === ast)
  }

  test("parse multiple element csv with mixed quotes, inner quotes") {
    val csv = "\"test\",\"more\"\"test\""
    val ast = A(A("test", "more\"test"))
    assert(C(csv) === ast)
  }

  test("parse multi-line csv") {
    val csv = """|this is,a test
                 |"csv",file
                 |with complex,"and ugly"
                 |">""<", formatting""".stripMargin.replace("\n", "\r\n")
    val ast = A(
      A("this is", "a test"),
      A("csv", "file"),
      A("with complex", "and ugly"),
      A(">\"<", " formatting")
    )
    assert(C(csv) === ast)
  }

  test("invalid csv throws exception") {
    intercept[ParsingException] {
      C("\"unclosed quote")
    }
  }
}
