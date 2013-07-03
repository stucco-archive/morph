package converter.parser

import converter.ast._

import com.github.asilvestre.jxmltojson.JsonConverter
import com.github.asilvestre.jpurexml.XmlParseException

/** Implements an XML parser that constructs an AST.
  *
  * Instead of implementing an XML parser from scratch, this
  * parser uses an already existing implementation of an XML
  * parser to convert XML to JSON, and then uses the
  * converter.parser.JsonParser to convert that to a native AST.
  *
  * XML attributes are prepended with an `@`, and content can be found
  * in `#content`.
  *
  * Arrays will be transformed to JSON arrays, and the name of the original
  * array tag will have an `s` appended to it.
  *
  * @author Anish Athalye
  */
object XmlParser extends AstBuilder {

  /** The main parsing method.
    *
    * @param input The content to parse.
    *
    * @return The root of the generated AST.
    */
  def apply(input: String) = {
    try {
      val json = JsonConverter convertXml input.mkString
      JsonParser(json)
    } catch {
      case ex: XmlParseException => throw ParsingException(ex.getMessage)
    }
  }

  /** The main parsing method.
    *
    * @param input The content to parse.
    *
    * @return The root of the generated AST.
    */
  def apply(input: Array[Char]) = apply(input.mkString)
}
