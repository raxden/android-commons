package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun LocalDate.toMilliseconds(): Long =
  atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDate.toSeconds(): Long =
  atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000

fun LocalDateTime.toSeconds(): Long =
  atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000

fun LocalDateTime.toMilliseconds(): Long = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun Long.toLocalDate(): LocalDate =
  Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun Long.toLocalDateTime(): LocalDateTime =
  Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()

fun LocalDate.isToday(): Boolean = this == LocalDate.now()

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

fun LocalDateTime.simpleFormat() = format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

fun LocalDateTime.format(pattern: String = "dd MMM yyyy") = format(DateTimeFormatter.ofPattern(pattern))

fun LocalDate.simpleFormat() = format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

fun LocalDate.format(pattern: String = "dd MMM yyyy") = format(DateTimeFormatter.ofPattern(pattern))
