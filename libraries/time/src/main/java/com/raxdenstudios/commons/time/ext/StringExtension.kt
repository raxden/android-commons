package com.raxdenstudios.commons.time.ext

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Parses this string as a [LocalDate] by trying each pattern in order and returning the first
 * match, or `null` when none matches.
 *
 * Extensions on [String] that parse a date with java.time. Parsing a non-matching input never
 * throws: it returns `null`. The only exception is an invalid pattern string, see below. These
 * are `java.time` extensions. The library needs no initialisation and targets minSdk 26+ or
 * projects that enable core library desugaring.
 *
 * Each pattern is compiled with `DateTimeFormatter.ofPattern` in the default format locale.
 * Patterns are compiled one at a time while scanning, so an invalid pattern throws
 * [IllegalArgumentException] only if no earlier pattern matched. Use the [DateTimeFormatter]
 * overload to avoid that: it never throws.
 */
fun String.toLocalDate(
    vararg patterns: String,
): LocalDate? {
    patterns.forEach { format ->
        val dateTimeFormatter = DateTimeFormatter.ofPattern(format)
        toLocalDate(dateTimeFormatter)?.also { localDate -> return localDate }
    }
    return null
}

/**
 * Parses this string as a [LocalDate] by trying each [DateTimeFormatter] in order and returning
 * the first match, or `null` when none matches.
 *
 * Any failure while parsing, including a string that does not match, returns `null` and the
 * cause is not reported. This overload never throws for bad input.
 */
fun String.toLocalDate(
    vararg formatters: DateTimeFormatter,
): LocalDate? {
    formatters.forEach { dateTimeFormatter ->
        runCatching { LocalDate.parse(this, dateTimeFormatter) }
            .getOrNull()?.also { localDate -> return localDate }
    }
    return null
}
