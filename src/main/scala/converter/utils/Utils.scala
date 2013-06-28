package converter.utils

object Utils {
  implicit class StringUtils(str: String) {
    def indent: String = indent(1)
    def indent(num: Int): String =
      " " * num + str.replace("\n", "\n" + " " * num)
  }
}
