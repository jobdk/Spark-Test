package Utils

object Utils {
  def checkLineForPossibleParsingErrors(line: String): String = {
    try {
      if (line == null) return null
      val lineSubstring: String =
        if (line.startsWith(",")) line.substring(1, line.length) else line
      if (line.contains("\uFFFF")) lineSubstring.replace("\uFFFF", "x")
      else lineSubstring
    } catch {
      case _: Exception =>
        null
    }
  }
}
