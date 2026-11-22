package com.raxdenstudios.commons.time.ext

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.util.TimeZone

/**
 * Returns the whole seconds since the Unix epoch of the start of this date in [timeZone].
 *
 * Extensions on [LocalDate] for epoch conversion, the today check and formatting. They are
 * `java.time` extensions. The library needs no initialisation and targets minSdk 26+ or
 * projects that enable core library desugaring.
 *
 * The start of day is the earliest valid time on this date in that zone. Fractional
 * seconds are dropped, so negative results are truncated toward zero. Defaults to the
 * JVM default zone ([TimeZone.getDefault]).
 *
 * ## Setup
 *
 * Add the `commons-time` dependency. No initialisation is required.
 *
 * ## Usage
 *
 * ```kotlin
 * val date = LocalDate.of(2024, 3, 15)
 * val millis = date.toMilliseconds()          // start of day in the default time zone
 * val seconds = date.toSeconds(TimeZone.getTimeZone("UTC"))
 * val label = date.toFormat("dd/MM/yyyy")     // "15/03/2024"
 * val isToday = date.isToday()
 * ```
 */
fun LocalDate.toSeconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = toMilliseconds(timeZone) / MILLIS_IN_SECOND

/**
 * Returns the milliseconds since the Unix epoch of the start of this date in [timeZone].
 *
 * The start of day is the earliest valid time on this date in that zone, so zones whose
 * midnight is skipped by a DST change still produce a value. Defaults to the JVM default
 * zone ([TimeZone.getDefault]).
 */
fun LocalDate.toMilliseconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = atStartOfDay().atZone(timeZone.toZoneId()).toInstant().toEpochMilli()

/**
 * Returns `true` when this date equals today in the system default zone (`LocalDate.now()`).
 *
 * This check does not take a [TimeZone] parameter, unlike the other extensions in this file,
 * so it can disagree with them near midnight in a non-default zone.
 */
fun LocalDate.isToday(): Boolean = this == LocalDate.now()

/**
 * Formats this date with [pattern], using the default format locale of the JVM.
 *
 * Defaults to `dd MMM yyyy`. An invalid pattern throws [IllegalArgumentException]. A
 * pattern that uses fields a [LocalDate] does not carry, such as `HH`, throws
 * [java.time.DateTimeException].
 */
fun LocalDate.toFormat(
    pattern: String = "dd MMM yyyy",
): String = format(DateTimeFormatter.ofPattern(pattern))

/** Formats this date with the localized [style] of [locale], e.g. "March 15, 2024" for LONG in en-US. */
fun LocalDate.toLocalizedString(
    style: FormatStyle = FormatStyle.LONG,
    locale: Locale = Locale.getDefault(),
): String = format(DateTimeFormatter.ofLocalizedDate(style).withLocale(locale))

private const val MILLIS_IN_SECOND = 1000L
