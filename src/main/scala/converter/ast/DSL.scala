package converter.ast

import scalaz._
import Scalaz._

object DSL {

	/** An implicit class that provides the implementation of the DSL for
	  * manipulating AST nodes.
	  */
	implicit class RichValueNode(node: ValueNode) {

		/** Returns the value corresponding to the specified key in an ObjectNode.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return The value wrapped in an Option[ValueNode].
		  */
		def \ (key: String): Option[ValueNode] = node match {
			case ObjectNode(fields) => fields get key
			case _ => None
		}

		/** Recursively searches for a value corresponding to the specified key.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return A list of all matching nodes.
		  */
		def \\ (key: String): ArrayNode = (node match {
			case ObjectNode(fields) => {
				val sub = fields.toList map {
					case (k, v) => (v \\ key).elements
				}
				fields get key match {
					case Some(value) => value :: sub.flatten
					case None => sub.flatten
				}
			}
			case ArrayNode(elem) => {
				val sub = elem map { _ \\ key } map { _.elements }
				sub.flatten
			}
			case _ => Nil
		}) |> { ArrayNode(_) }

		/** Recursively finds all nodes that match a given predicate.
		  *
		  * @param pred The predicate.
		  *
		  * @return A list of all nodes that match the predicate.
		  */
		def find(pred: ValueNode => Boolean): ArrayNode = (node match {
			case ObjectNode(fields) => {
				val submap = fields.toList map {
					case (k, v) => (v find pred).elements
				}
				val sub = submap.flatten
				if (pred(node)) node :: sub else sub
			}
			case ArrayNode(elem) => {
				val submap = elem map { v => (v find pred).elements }
				val sub = submap.flatten
				if (pred(node)) node :: sub else sub
			}
			case _ => if (pred(node)) List(node) else Nil
		}) |> { ArrayNode(_) }
	}

	/** An implicit class that provides certain methods on `Option[ValueNode]`
	  * to allow chaining of those methods that return an `Option[ValueNode]`.
	  */
	implicit class RichOptionValueNode(opt: Option[ValueNode]) {

		/** Returns the value corresponding to the specified key in an ObjectNode.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return The value wrapped in an Option[ValueNode].
		  */
		def \ (key: String): Option[ValueNode] = opt flatMap { _ \ key }

		/** Recursively searches for a value corresponding to the specified key.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return A list of all matching nodes.
		  */
		def \\ (key: String): ArrayNode = opt match {
			case Some(node) => node \\ key
			case None => ArrayNode()
		}
	}
}
