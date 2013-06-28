import org.scalatest.FunSuite

import converter.parser.CsvParser
import converter.ast._
import converter.ast.Implicits._

class CsvParserSuite extends FunSuite {

  // to make testing easier
  def A = ArrayNode
  def C = CsvParser

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
                 |">""<", formatting""".stripMargin.replace("\n","\r\n")
    val ast = A(
      A("this is", "a test"),
      A("csv", "file"),
      A("with complex", "and ugly"),
      A(">\"<", " formatting")
    )
    assert(C(csv) === ast)
  }
}
