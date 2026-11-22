package com.raxdenstudios.commons.time.ext

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle

/**
 * Parses this string as a [LocalDate] by trying each pattern in order and returning the first
 * match, or `null` when none matches.
 *
 * Extensions on [String] that parse a date with java.time. Parsing never throws: a non-matching
 * input or an invalid pattern returns `null`. These are `java.time` extensions. The library needs
 * no initialisation and targets minSdk 26+ or projects that enable core library desugaring.
 *
 * Each pattern is compiled with `DateTimeFormatter.ofPattern` in the default format locale and
 * resolved with [ResolverStyle.STRICT], so impossible dates such as "31/02/2024" return `null`
 * instead of being adjusted. As STRICT needs an era for `y` (year-of-era), patterns without `G`
 * use `u` (proleptic year) instead, so "dd/MM/yyyy" keeps working. An invalid pattern is skipped.
 */
fun String.toLocalDate(
    vararg patterns: String,
): LocalDate? {
    patterns.forEach { format ->
        val dateTimeFormatter = format.toStrictFormatter() ?: return@forEach
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

private fun String.toStrictFormatter(): DateTimeFormatter? = runCatching {
    DateTimeFormatter.ofPattern(withProlepticYear()).withResolverStyle(ResolverStyle.STRICT)
}.getOrNull()

/** Replaces the year-of-era letter `y` with `u` outside quoted literals, unless the pattern has an era `G`. */
private fun String.withProlepticYear(): String {
    var quoted = false
    val outsideQuotes = map { char ->
        if (char == QUOTE) quoted = !quoted
        !quoted
    }
    val hasEra = indices.any { index -> outsideQuotes[index] && this[index] == ERA }
    if (hasEra) return this
    return mapIndexed { index, char ->
        if (outsideQuotes[index] && char == YEAR_OF_ERA) PROLEPTIC_YEAR else char
    }.joinToString(separator = "")
}

private const val QUOTE = '\''
private const val ERA = 'G'
private const val YEAR_OF_ERA = 'y'
private const val PROLEPTIC_YEAR = 'u'
