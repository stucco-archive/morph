package morph.utils

/** A collection of various utilities.
  *
  * @author Anish Athalye
  */
object Utils {

  /** An implicit class to provide additional methods on `String`.
    *
    * @author Anish Athalye
    */
  implicit class StringUtils(str: String) {

    /** Indent every line in a string by one space.
      *
      * @return The indented string.
      */
    def indent: String = indent(2)

    /** Indent every line in a string by a specified number of spaces.
      *
      * @param num The number of spaces to indent every line by.
      *
      * @return The indented string.
      */
    def indent(num: Int): String =
      " " * num + str.replace("\n", "\n" + " " * num)

    /** Escape the escape codes in the string to be suitable for printing
      * or displaying the escape sequences.
      *
      * @return The string with escapes visibly escaped.
      */
    def escaped: String = str flatMap {
      case '\b' => "\\b"
      case '\f' => "\\f"
      case '\n' => "\\n"
      case '\r' => "\\r"
      case '\t' => "\\t"
      case '\\' => "\\\\"
      case '"' => "\\\""
      case c => c.toString
    }
  }
}
