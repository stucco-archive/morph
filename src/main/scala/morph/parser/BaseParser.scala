package morph.parser

import morph.ast._

import org.parboiled.scala._
import org.parboiled.errors.ErrorUtils

/** The base class of all parsers.
  *
  * @author Anish Athalye
  */
abstract class BaseParser extends Parser with AstBuilder {

  /** The root parsing rule.
    */
  def RootRule: Rule1[ValueNode]

  /** The main parsing method.
    *
    * Uses a ReportingParseRunner (which only reports the first error)
    * for simplicity.
    *
    * @param input The content to parse.
    *
    * @return The root of the generated AST.
    */
  def apply(input: String): ValueNode = apply(input.toCharArray)
  
  /** The main parsing method.
    *
    * Uses a ReportingParseRunner (which only reports the first error)
    * for simplicity.
    *
    * @param input The content to parse.
    *
    * @return The root of the generated AST.
    */
  def apply(input: Array[Char]): ValueNode = {
    val parsingResult = ReportingParseRunner(RootRule).run(input)
    parsingResult.result getOrElse {
      throw ParsingException("Invalid source:\n" +
        ErrorUtils.printParseErrors(parsingResult))
    }
  }
}
