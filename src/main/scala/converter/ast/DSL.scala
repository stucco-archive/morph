package converter.ast

import scala.language.implicitConversions

import scalaz._
import Scalaz._

/** An implementation of a DSL for manipulating and searching an AST.
  *
  * To be able to use the DSL, it is necessary to import the inner nested
  * classes by doing `import converter.ast.DSL._`.
  *
  * @author Anish Athalye
  */
object DSL {

	/** An implicit class that provides the implementation of the DSL for
	  * manipulating AST nodes.
    *
    * @author Anish Athalye
	  */
	implicit class RichValueNode(node: ValueNode) {

    /** Returns the fields of an ObjectNode.
      *
      * @throws IllegalArgumentException If the node is not an ObjectNode.
      */
    def fields: Map[String, ValueNode] = node match {
      case ObjectNode(fields) => fields
      case _ => throw new
        IllegalArgumentException("getting field of non-object node")
    }

    /** Returns the elements of an ArrayNode.
      *
      * @throws IllegalArgumentException If the node is not an ArrayNode.
      */
    def elements: List[ValueNode] = node match {
      case ArrayNode(elem) => elem
      case _ => throw new
        IllegalArgumentException("getting elements of non-array node")
    }

    /** Returns the value corresponding to the specified key in an ObjectNode.
      *
      * @throws NoSuchElementException If the key does not exist.
      * @throws IllegalArgumentException If the node is not an ObjectNode.
      *
      * @param key The key corresponding to the value to retrieve.
      */
    def -> (key: String): ValueNode = node match {
      case ObjectNode(fields) => fields(key)
      case _ => throw new
        IllegalArgumentException("field access of non-object node")
    }

    /** Returns the value corresponding to the specified key in an ObjectNode.
      *
      * @throws NoSuchElementException If the key does not exist.
      * @throws IllegalArgumentException If the node is not an ObjectNode.
      *
      * @param key The key corresponding to the value to retrieve.
      */
    def get(key: String): ValueNode = node -> key


    /** Returns the element with the specified index in an ArrayNode.
      *
      * @throws IndexOutOfBoundsException
      * @throws IllegalArgumentException If the node is not an ObjectNode.
      *
      * @param index The index of the element to retrieve.
      */
    def +> (index: Int): ValueNode = node match {
      case ArrayNode(elem) => elem(index)
      case _ => throw new
        IllegalArgumentException("element access of non-array node")
    }

    /** Returns the element with the specified index in an ArrayNode.
      *
      * @throws IndexOutOfBoundsException
      * @throws IllegalArgumentException If the node is not an ObjectNode.
      *
      * @param index The index of the element to retrieve.
      */
    def get(index: Int): ValueNode = node +> index

		/** Returns the value corresponding to the specified key in an ObjectNode.
      *
      * Safely searches an object for a key.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return The value wrapped in an Option[ValueNode].
		  */
		def ~> (key: String): Option[ValueNode] = node match {
			case ObjectNode(fields) => fields get key
			case _ => None
		}

		/** Returns the value corresponding to the specified key in an ObjectNode.
      *
      * Safely searches an object for a key.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return The value wrapped in an Option[ValueNode].
		  */
    def optget(key: String): Option[ValueNode] = node ~> key

		/** Recursively searches for a value corresponding to the specified key.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return A list of all matching nodes.
		  */
    def ->> (key: String): ArrayNode = (node match {
			case ObjectNode(fields) => {
				val sub = fields.toList map {
          case (k, v) => (v ->> key).elements
				}
				fields get key match {
					case Some(value) => value :: sub.flatten
					case None => sub.flatten
				}
			}
			case ArrayNode(elem) => {
        val sub = elem map { _ ->> key } map { _.elements }
				sub.flatten
			}
			case _ => Nil
		}) |> { ArrayNode(_) }

		/** Recursively searches for a value corresponding to the specified key.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return A list of all matching nodes.
		  */
    def recget(key: String): ArrayNode = node ->> key

		/** Recursively finds all nodes that match a given predicate.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.

		  * @param predicate The predicate.
		  *
		  * @return A list of all nodes that satisfy the predicate.
		  */
    def -?> (predicate: ValueNode => Boolean): ArrayNode = (node match {
			case ObjectNode(fields) => {
				val submap = fields.toList map {
          case (k, v) => (v -?> predicate).elements
				}
				val sub = submap.flatten
				if (predicate(node)) node :: sub else sub
			}
			case ArrayNode(elem) => {
        val submap = elem map { v => (v -?> predicate).elements }
				val sub = submap.flatten
				if (predicate(node)) node :: sub else sub
			}
			case _ => if (predicate(node)) List(node) else Nil
		}) |> { ArrayNode(_) }

		/** Recursively finds all nodes that match a given predicate.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.

		  * @param predicate The predicate.
		  *
		  * @return A list of all nodes that satisfy the predicate.
		  */
    def find(predicate: ValueNode => Boolean): ArrayNode = node -?> predicate

    /** Map a function onto an array node.
      *
      * @throws IllegalArgumentException If the node is not an ArrayNode.
      *
      * @param func The function to map onto the array.
      */
    def +*> (func: ValueNode => ValueNode): ArrayNode = node match {
      case ArrayNode(elem) => ArrayNode(elem map func)
      case _ => throw new
        IllegalArgumentException("element access of non-array node")
    }

    /** Map a function onto an array node.
      *
      * @throws IllegalArgumentException If the node is not an ArrayNode.
      *
      * @param func The function to map onto the array.
      */
    def map(func: ValueNode => ValueNode): ArrayNode = node +*> func

    /** Filter an array node by a predicate.
      *
      * @throws IllegalArgumentException If the node is not an ArrayNode.
      *
      * @param predicate The predicate to filter by.
      */
    def +/> (predicate: ValueNode => Boolean): ArrayNode = node match {
      case ArrayNode(elem) => ArrayNode(elem filter predicate)
      case _ => throw new
        IllegalArgumentException("element access of non-array node")
    }

    /** Filter an array node by a predicate.
      *
      * @throws IllegalArgumentException If the node is not an ArrayNode.
      *
      * @param predicate The predicate to filter by.
      */
    def filter(predicate: ValueNode => Boolean): ArrayNode = node +/> predicate
	}
  
	/** An implicit class that provides certain methods on `Option[ValueNode]`
	  * to allow chaining of those methods that return an `Option[ValueNode]`.
    *
    * @author Anish Athalye
	  */
	implicit class RichOptionValueNode(opt: Option[ValueNode]) {

		/** Returns the value corresponding to the specified key in an ObjectNode.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return The value wrapped in an Option[ValueNode].
		  */
    def ~> (key: String): Option[ValueNode] = opt flatMap { _ ~> key }

		/** Returns the value corresponding to the specified key in an ObjectNode.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return The value wrapped in an Option[ValueNode].
		  */
    def optget(key: String): Option[ValueNode] = opt ~> key

		/** Recursively searches for a value corresponding to the specified key.
		  *
		  * @note This method is not tail recursive, so it is necessary to be
		  * aware of potential stack overflow problems.
		  *
		  * @param key The key corresponding to the value to retrieve.
		  *
		  * @return A list of all matching nodes.
		  */
    def ->> (key: String): ArrayNode = opt match {
      case Some(node) => node ->> key
			case None => ArrayNode()
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
    def recget(key: String): ArrayNode = opt ->> key
	}

  /** An implicit conversion from `Option[ValueNode]` to
    * an `Either[...]` to help have a type safe `^` object constructor
    * and `*` array constructor.
    */
  implicit def OptionValueNodeToEither(
      opt: Option[ValueNode]): Either[Option[ValueNode], ValueNode] =
    Left(opt)

  /** An implicit conversion from `ValueNode` to
    * an `Either[...]` to help have a type safe `^` object constructor
    * and `*` array constructor.
    */
  implicit def ValueNodeToEither(
      value: ValueNode): Either[Option[ValueNode], ValueNode] =
    Right(value)

  /** ObjectNode with mixed `ValueNode` and `Option[ValueNode]` constructor.
    *
    * Can be used to construct literal objects like `^("a" -> "b", ...)`
    *
    * This constructor provides very concise syntax and flexibility,
    * and it is completely type safe.
    */
  object ^ {

    def apply(members: (String, Either[Option[ValueNode], ValueNode])*) = {
      val filtered = members withFilter {
        case (k, Left(opt)) => opt.nonEmpty
        case _ => true
      }
      val flattened = filtered map {
        case (k, Left(opt)) => k -> opt.get
        case (k, Right(value)) => k -> value
      }
      ObjectNode(flattened: _*)
    }
  }

  /** ArrayNode with mixed `ValueNode` and `Option[ValueNode]` constructor.
    *
    * Can be used to construct literal arrays like `*("a", "b", ...)`
    *
    * This constructor provides very concise syntax and flexibility,
    * and it is completely type safe.
    */
  object * {

    def apply(elements: Either[Option[ValueNode], ValueNode]*) = {
      val filtered = elements withFilter {
        case Left(opt) => opt.nonEmpty
        case _ => true
      }
      val flattened = filtered map {
        case Left(opt) => opt.get
        case Right(value) => value
      }
      ArrayNode(flattened: _*)
    }
  }
}
