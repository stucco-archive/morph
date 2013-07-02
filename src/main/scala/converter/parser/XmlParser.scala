package converter.parser

import converter.ast._

import net.sf.json.xml.XMLSerializer

/** Implements an XML parser that constructs an AST.
  *
  * Instead of implementing an XML parser from scratch, this
  * parser uses an already existing implementation of an XML
  * parser to convert XML to JSON, and then uses the
  * converter.parser.JsonParser to convert that to a native AST.
  *
  * @author Anish Athalye
  */
object XmlParser extends AstBuilder {

  def apply(input: Array[Char]) = {
    val parser = new XMLSerializer()
    val json = parser read input.mkString
    JsonParser(json toString 0)
  }
}
