package morph.extractor

import morph.ast._

/**
 * The base class that all extractors should extend.
 * 
 * This class mixes in the DSL and AST-related implicit conversions
 * so that subclasses can omit those imports.
 *
 * Subclasses must define the extract method, which takes a ValueNode
 * and performs extractions / transformations on it.
 *
 * @author Anish Athalye
 */
abstract class Extractor extends DSL with Implicits {

  /**
   * The main extraction / transformation method.
   *
   * @param node The node to transform.
   *
   * @return A transformed node.
   */
  final def apply(node: ValueNode): ValueNode = extract(node)

  /**
   * The main extraction / transformation method.
   *
   * @param node The node to transform.
   *
   * @return A transformed node.
   */
  def extract(node: ValueNode): ValueNode
}
