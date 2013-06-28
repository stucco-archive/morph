package converter.ast

import converter.utils.Utils._

import collection.immutable.ListMap

/** General type of an AST node.
  */
sealed abstract class ValueNode

/** An object.
  */
case class ObjectNode(fields: Map[String, ValueNode]) extends ValueNode {
  
  override def toString = {
    val mapStr = fields map { case (k, v) => "\"" + k + "\": " + v } mkString ",\n"
    "{\n" + mapStr.indent + "\n}"
  }

  /** Get a list of the contents corresponding to the specified field names.
    *
    * If certain fields do not exist, their entries will be removed from the
    * resulting list, which will collapse the list.
    *
    * @param names The names of the fields to retrieve.
    *
    * @return The fields corresponding to the given names.
    */
  def getFields(names: String*): Seq[ValueNode] = names flatMap fields.get
}

object ObjectNode {

  def apply(members: (String, ValueNode)*) =
    new ObjectNode(ListMap(members: _*))

  def apply(members: List[(String, ValueNode)]) =
    new ObjectNode(ListMap(members: _*))
}

/** An array.
  */
case class ArrayNode(elements: List[ValueNode]) extends ValueNode {

  override def toString = "[\n" + elements.mkString(",\n").indent + "\n]"
}

object ArrayNode {

  def apply(elements: ValueNode*) = new ArrayNode(elements.toList)
}

/** A string.
  */
case class StringNode(value: String) extends ValueNode {

  override def toString = "\"" + value + "\""
}

object StringNode {
  
  def apply(sym: Symbol) = new StringNode(sym.name)
}

/** A number.
  *
  * A generic number type represented as a BigDecimal.
  */
case class NumberNode(value: BigDecimal) extends ValueNode {

  override def toString = value.toString
}

object NumberNode {

  def apply(n: Int) = new NumberNode(BigDecimal(n))

  def apply(n: Long) = new NumberNode(BigDecimal(n))

  def apply(n: Double) = n match {
    case n if n.isNaN => NullNode
    case n if n.isInfinity => NullNode
    case _ => new NumberNode(BigDecimal(n))
  }

  def apply(n: BigInt) = new NumberNode(BigDecimal(n))

  def apply(n: String) = new NumberNode(BigDecimal(n))
}

sealed abstract class BooleanNode extends ValueNode {

  def value: Boolean

  override def toString = value.toString
}

/** A boolean.
  */
object BooleanNode {
  def apply(x: Boolean): BooleanNode =
    if (x) TrueNode else FalseNode
  
  def unapply(x: BooleanNode): Option[Boolean] = Some(x.value)
}

case object TrueNode extends BooleanNode {
  def value = true
}

case object FalseNode extends BooleanNode {
  def value = false
}

/** A null value.
  */
case object NullNode extends ValueNode {

  override def toString = "null"
}

/** TEMPORARY
  */
object Test {
  val obj = ObjectNode(Map(
    "awesome" -> TrueNode,
    "str" -> StringNode("stringy"),
    "moremaps" -> ObjectNode(Map("yes" -> StringNode("absolutely"))),
    "list" -> ArrayNode(List(StringNode("a string"), NumberNode(3.1415)))
  ))
}
