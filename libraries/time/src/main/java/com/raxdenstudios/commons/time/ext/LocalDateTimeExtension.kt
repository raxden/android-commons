package com.raxdenstudios.commons.time.ext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

/**
 * Returns the whole seconds since the Unix epoch of this date-time interpreted in [timeZone].
 *
 * Extensions on [LocalDateTime] for epoch conversion and formatting. They are the `java.time`
 * replacements for the `commons-threetenabp` extensions and keep the same API. The library
 * needs no initialisation and targets minSdk 26+ or projects that enable core library
 * desugaring.
 *
 * Fractional seconds are dropped, so negative results are truncated toward zero. In a DST
 * gap the time is shifted later by the gap length; in an overlap the earlier offset is used.
 * Defaults to the JVM default zone ([TimeZone.getDefault]).
 */
fun LocalDateTime.toSeconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = toMilliseconds(timeZone) / MILLIS_IN_SECOND

/**
 * Returns the milliseconds since the Unix epoch of this date-time interpreted in [timeZone].
 *
 * DST gaps and overlaps resolve the same way as [toSeconds]. Defaults to the JVM default
 * zone ([TimeZone.getDefault]).
 */
fun LocalDateTime.toMilliseconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = atZone(timeZone.toZoneId()).toInstant().toEpochMilli()

/**
 * Formats this date-time with [pattern], using the default format locale of the JVM.
 *
 * Defaults to `dd MMM yyyy`, which omits the time; pass a pattern with time fields to show
 * them. An invalid pattern throws [IllegalArgumentException].
 */
fun LocalDateTime.toFormat(
    pattern: String = "dd MMM yyyy",
): String = format(DateTimeFormatter.ofPattern(pattern))

private const val MILLIS_IN_SECOND = 1000L
