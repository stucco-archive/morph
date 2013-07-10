package morph.parser

import morph.ast._

import io.Source

/** An interface to make using parsers easy.
  *
  * @author Anish Athalye
  */
object Interface {

  /** An object to help parse various data sources.
    */
  object parse {

    /** Parse a string.
      */
    def string(str: String): Parsable = new Parsable(str)

    /** Parse a file specified by a given path.
      */
    def file(path: String): Parsable = {
      val source = Source fromFile path
      val data = source.getLines mkString "\n"
      source.close()
      new Parsable(data)
    }
  }

  /** A class that holds parsable data.
    */
  class Parsable(data: String) {

    /** Parse data using a specific parser.
      */
    def using(parser: AstBuilder): ValueNode = parser(data)
  }

}
