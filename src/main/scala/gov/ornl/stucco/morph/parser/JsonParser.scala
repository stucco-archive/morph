package gov.ornl.stucco.morph.parser

import gov.ornl.stucco.morph.ast._

import org.parboiled.scala._
import org.parboiled.Context

import java.lang.StringBuilder

/**
 * A JSON parser that constructs an AST.
 *
 * It can parse JSON files that conform to the specification available
 * at www.json.org. This implimentation almost directly follows the
 * grammar specified there.
 *
 * @author Anish Athalye
 */
object JsonParser extends BaseParser with WhiteSpaceExpansion {

  def RootRule = Json

  lazy val Json = rule { WhiteSpace ~ Value ~ EOI }

  def JsonObject = rule {
    "{ " ~ zeroOrMore(Pair, separator = ", ") ~ "} " ~~> { ObjectNode(_: _*) }
  }

  def Pair = rule { JsonStringUnwrapped ~ ": " ~ Value ~~> { (_, _) } }

  def Value: Rule1[ValueNode] = rule {
    JsonString | JsonNumber | JsonObject | JsonArray |
      JsonTrue | JsonFalse | JsonNull
  }

  def JsonString = rule { JsonStringUnwrapped ~~> { StringNode(_) } }

  def JsonStringUnwrapped = rule {
    "\"" ~ Characters ~ "\" " ~~> { _.toString }
  }

  def JsonNumber = rule {
    group(Integer ~ optional(Frac) ~ optional(Exp)) ~>
      { NumberNode(_) } ~ WhiteSpace
  }

  def JsonArray = rule {
    "[ " ~ zeroOrMore(Value, separator = ", ") ~ "] " ~~> { ArrayNode(_) }
  }

  def Characters = rule {
    push(new StringBuilder) ~ zeroOrMore("\\" ~ EscapedChar | NormalChar)
  }

  def EscapedChar = rule {
    anyOf("\"\\/") ~:% withContext(appendToSb(_)(_)) |
      "b" ~ appendToSb('\b') |
      "f" ~ appendToSb('\f') |
      "n" ~ appendToSb('\n') |
      "r" ~ appendToSb('\r') |
      "t" ~ appendToSb('\t') |
      Unicode ~~% {
        withContext((code, ctx) => appendToSb(code.asInstanceOf[Char])(ctx))
      }
  }

  def NormalChar = rule {
    !anyOf("\"\\") ~ ANY ~:% { withContext(appendToSb(_)(_)) }
  }

  def Unicode = rule {
    "u" ~ group(HexDigit ~ HexDigit ~ HexDigit ~ HexDigit) ~>
      { java.lang.Integer.parseInt(_, 16) }
  }

  def Integer = rule { optional("-") ~ (("1" - "9") ~ Digits | Digit) }

  def Digits = rule { oneOrMore(Digit) }

  def Digit = rule { "0" - "9" }

  def HexDigit = rule { Digit | "a" - "f" | "A" - "F" }

  def Frac = rule { "." ~ Digits }

  def Exp = rule { ignoreCase("e") ~ optional(anyOf("+-")) ~ Digits }

  def JsonTrue = rule { "true " ~ push(TrueNode) }

  def JsonFalse = rule { "false " ~ push(FalseNode) }

  def JsonNull = rule { "null " ~ push(NullNode) }

  def appendToSb(c: Char): Context[Any] => Unit = { ctx =>
    ctx.getValueStack.peek.asInstanceOf[StringBuilder].append(c)
  }
}
