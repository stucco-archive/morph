package morph.extractor

import morph.ast._

/**
 * The base class of all extractors.
 * @author Anish Athalye
 */
abstract class BaseExtractor extends DSL with Implicits {

  /**
   * The main extraction / transformation method.
   */
  final def apply(node: ValueNode): ValueNode = extract(node)

  /**
   * The main extraction / transformation method.
   */
  def extract(node: ValueNode): ValueNode
}
