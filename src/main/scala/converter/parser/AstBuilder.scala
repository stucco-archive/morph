package converter.parser

import converter.ast._

/** A trait describing methods that AST builders must implement.
  *
  * @author Anish Athalye
  */
trait AstBuilder {
  
  /** The main parsing method.
    *
    * @param input The content to parse.
    *
    * @return The root of the generated AST.
    */
  def apply(input: String): ValueNode

  /** The main parsing method.
    *
    * @param input The content to parse.
    *
    * @return The root of the generated AST.
    */
  def apply(input: Array[Char]): ValueNode
}
