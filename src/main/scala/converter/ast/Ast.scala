sealed abstract class ValueNode

case class ObjectNode(fields: Map[String, ValueNode]) extends ValueNode

case class ArrayNode(elements: List[ValueNode]) extends ValueNode

case class StringNode(value: String) extends ValueNode

case class NumberNode(value: BigDecimal) extends ValueNode

sealed abstract class BooleanNode extends ValueNode {
  def value: Boolean
}

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

case object NullNode extends ValueNode
