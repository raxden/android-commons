package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun String.toLocalDate(vararg pattern: String): LocalDate? {
  pattern.forEach { format ->
    try {
      LocalDate.parse(this, DateTimeFormatter.ofPattern(format))?.also { return it }
    } catch (e: Exception) {
    }
  }
  return null
}

fun String.toLocalDate(vararg formatter: DateTimeFormatter): LocalDate? {
  formatter.forEach { dtf ->
    try {
      LocalDate.parse(this, dtf)?.also { return it }
    } catch (e: Exception) {
    }
  }
  return null
}
