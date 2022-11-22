package utils

object ReaderUtils {
  def checkLineForPossibleParsingErrors(line: String): String= {
    val lineSubstring: String =
      if (line.startsWith(",")) line.substring(1, line.length) else line
    if (line.contains("\uFFFF")) lineSubstring.replace("\uFFFF", "x")
    else lineSubstring
  }
}
