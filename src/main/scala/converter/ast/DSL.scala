package converter.ast

import converter.ast.{ValueNode => VN}

import scala.language.implicitConversions
import scala.{PartialFunction => PF}

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

  /** An implicit conversion from a type viewable as a `ValueNode` to
    * `Option[ValueNode]`.
    */
  implicit def ValueNodeViewable2OptionValueNode[T <% VN](
      nodeViewable: T): Option[VN] =
    Option(nodeViewable)

  /** An implicit conversion from any type that can be viewed as a `ValueNode`
    * to an `RichOptionValueNode.
    *
    * This is necessary because Scala will never perform multiple implicit
    * conversions in a row. Without this, doing something like *(1) would not
    * work to construct an array containing a `NumberNode` because that would
    * require three implicit conversions `Int` to `ValueNode` to
    * `Option[ValueNode]` to `RichOptionValueNode`.
    *
    * Introducing another implicit conversion that can convert any type that is
    * viewable as a `ValueNode` into an `RichOptionValueNode` solves this
    * problem. This is done using a type parameter `T` that is viewable as a
    * `ValueNode` solves this problem.
    */
  implicit def ValueNodeViewable2RichOptionValueNode[T <% VN](
      nodeViewable: T): RichOptionValueNode =
    RichOptionValueNode(nodeViewable)
  
  /** An implicit class that provides the majority of the methods in the DSL.
    *
    * These methods operate on `Option`s because methods may or may not return
    * results. By having these methods on `Option`s, it is very easy to chain
    * them and automatically filter out empty results in the end using the
    * ^(...) object constructor or the *(...) array constructor.
    *
    * @author Anish Athalye
    */
  implicit class RichOptionValueNode(opt: Option[VN]) {

    /** Returns the value corresponding to the specified key in an object node.
      *
      * @param key The key corresponding to the value to retrieve.
      *
      * @return The value.
      */
    def get(key: String): Option[VN] = opt flatMap {
      case ObjectNode(fields) => fields get key
      case _ => None
    }

    /** Returns the value corresponding to the specified key in an ObjectNode.
      *
      * @param key The key corresponding to the value to retrieve.
      *
      * @return The value.
      */
    def ~> (key: String): Option[VN] = opt get key

    /** Returns the element corresponding to the specified index in an array node.
      *
      * @note This method uses 1-based indexing.
      */
    def get(index: Int): Option[VN] = opt flatMap {
      case ArrayNode(elem) => elem lift (index - 1)
      case _ => None
    }

    /** Returns the element corresponding to the specified index in an array node.
      *
      * @note This method uses 1-based indexing.
      */
    def ~> (index: Int): Option[VN] = opt get index

    /** Recursively searches for a value corresponding to the specified key.
      *
      * @note This method is not tail recursive, so it is necessary to be
      * aware of potential stack overflow problems.
      *
      * @param key The key corresponding to the value to retrieve.
      *
      * @return A list of all matching nodes.
      */
    def recGet(key: String): Option[ArrayNode] = {
      def iter(node: VN, key: String): List[VN] = node match {
        case ObjectNode(fields) => {
          val sub = fields.toList map {
            case (k, v) => iter(v, key)
          }
          fields get key match {
            case Some(value) => value :: sub.flatten
            case None => sub.flatten
          }
        }
        case ArrayNode(elem) => {
          val sub = elem map { iter(_, key) }
          sub.flatten
        }
        case _ => Nil
      }
      opt map { node => ArrayNode(iter(node, key)) }
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
    def ~>> (key: String): Option[ArrayNode] = opt recGet key

    /** Map a function over an array node.
      */
    def mapFunc(func: FuncVN2VN): Option[VN] = opt collect {
      case ArrayNode(elem) => ArrayNode(elem map func.inner)
    }

    /** Map a function over an array node.
      */
    def |-> (func: FuncVN2VN): Option[VN] = opt mapFunc func

    /** Map a partial function over an array node.
      */
    def mapFunc(func: PFuncVN2VN): Option[VN] = opt collect {
      case ArrayNode(elem) => ArrayNode(elem collect func.inner)
    }

    /** Map a partial function over an array node.
      */
    def |-> (func: PFuncVN2VN): Option[VN] = opt mapFunc func

    /** Map a function returning a mapping over an object node.
      */
    def mapFunc(func: FuncStringVN2StringVN): Option[VN] = opt collect {
      case ObjectNode(fields) => ObjectNode(fields map func.inner)
    }

    /** Map a function returning a mapping over an object node.
      */
    def |-> (func: FuncStringVN2StringVN): Option[VN] = opt mapFunc func

    /** Map a function returning a single element over an object node.
      */
    def mapFunc(func: FuncStringVN2VN): Option[VN] = opt collect {
      case ObjectNode(fields) => ArrayNode((fields map func.inner).toList)
    }

    /** Map a function returning a single element over an object node.
      */
    def |-> (func: FuncStringVN2VN): Option[VN] = opt mapFunc func

    /** Map a partial function returning a mapping over an object node.
      */
    def mapFunc(func: PFuncStringVN2StringVN): Option[VN] = {
      opt collect {
        case ObjectNode(fields) => ObjectNode(fields collect func.inner)
      }
    }

    /** Map a partial function returning a mapping over an object node.
      */
    def |-> (func: PFuncStringVN2StringVN): Option[VN] =
      opt mapFunc func

    /** Map a partial function returning a single element over an object node.
      */
    def mapFunc(func: PFuncStringVN2VN): Option[VN] = opt collect {
      case ObjectNode(fields) => ArrayNode((fields collect func.inner).toList)
    }

    /** Map a partial function returning a single element over an object node.
      */
    def |-> (func: PFuncStringVN2VN): Option[VN] = opt mapFunc func

    /** Apply a function to an value node or map the function over the
      * elements of an array node.
      *
      * This is useful for dealing with ambiguities between a single element
      * and an array of elements, as long as the inner element type is not
      * an array.
      */
    def applyOrMapFunc(func: FuncVN2VN): Option[VN] = opt flatMap {
      case arr: ArrayNode => arr mapFunc func
      case other => Option(func.inner(other))
    }

    /** Apply a function to an value node or map the function over the
      * elements of an array node.
      */
    def |+> (func: FuncVN2VN): Option[VN] =
      opt applyOrMapFunc func

    /** Apply a function to an value node or map the function over the
      * elements of an array node.
      *
      * This is useful for dealing with ambiguities between a single element
      * and an array of elements, as long as the inner element type is not
      * an array.
      */
    def applyOrMapFunc(func: PFuncVN2VN): Option[VN] = opt flatMap {
      case arr: ArrayNode => arr mapFunc func
      case other => func.inner.lift(other)
    }

    /** Apply a function to an value node or map the function over the
      * elements of an array node.
      */
    def |+> (func: PFuncVN2VN): Option[VN] =
      opt applyOrMapFunc func

    /** Returns true if the node is empty.
      */
    def nodeEmpty: Boolean = opt map {
      case ObjectNode(fields) => fields.isEmpty
      case ArrayNode(elem) => elem.isEmpty
      case StringNode(s) => s.isEmpty
      case NullNode => true
      case _ => false
    } getOrElse true

    /** Returns true if the node is nonempty.
      */
    def nodeNonEmpty: Boolean = !nodeEmpty
  }

  /** ObjectNode constructor from `(String, Option[ValueNode])*`.
    *
    * Can be used to construct literal objects like `^("a" -> "b", ...)`
    *
    * This constructor provides very concise syntax and flexibility,
    * and it is completely type safe.
    */
  object ^ {

    def apply(members: (String, Option[VN])*): ObjectNode = {
      val flattened = members collect { case (k, Some(v)) => k -> v }
      ObjectNode(flattened: _*)
    }
  }

  /** ArrayNode constructor from `(String, Option[ValueNode])*`.
    *
    * Can be used to construct literal arrays like `*("a", "b", ...)`
    *
    * This constructor provides very concise syntax and flexibility,
    * and it is completely type safe.
    */
  object * {

    def apply(elements: Option[VN]*): ArrayNode =
      ArrayNode(elements.flatten: _*)
  }

  // these constructs are needed for RichOptionValueNode.applyFunc
  // because of type erasure
  implicit class FuncVN2VN(val inner: VN => VN)

  implicit class FuncStringVN2StringVN(val inner: ((String, VN)) => (String, VN))

  implicit class FuncStringVN2VN(val inner: ((String, VN)) => VN)

  implicit class PFuncVN2VN(val inner: PF[VN, VN])

  implicit class PFuncStringVN2StringVN(val inner: PF[(String, VN), (String, VN)])

  implicit class PFuncStringVN2VN(val inner: PF[(String, VN), VN])
}
