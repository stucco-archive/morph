package morph.parser

import scala.language.implicitConversions

import org.parboiled.scala._

/** Mixin for Parsers that makes matching whitespace easier.
  *
  * When this trait is mixed in, all strings of the form `"text "` are
  * automatically converted to a rule that matches all trailing
  * whitespace characters as well.
  *
  * @author Anish Athalye
  */
trait WhiteSpaceExpansion {

  // self type, class mixing this in must be a Parser
  this: Parser =>

  /** Rule that matches all whitespace.
    *
    * Matches all whitespace including space, newline, carriage return,
    * tab, and form feed.
    */
  def WhiteSpace: Rule0 = rule { zeroOrMore(anyOf(" \n\r\t\f")) }

  /** Implicit conversion to make whitespace matching easier.
    *
    * When converting a string to a rule, if the string ends with a space,
    * convert it to a rule that matches any whitespace after the string.
    */
  override implicit def toRule(string: String) = {
    if (string.endsWith(" ")) str(string.trim) ~ WhiteSpace
    else str(string)
  }
}
