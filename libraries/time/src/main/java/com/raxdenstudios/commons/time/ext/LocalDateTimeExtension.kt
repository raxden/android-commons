package com.raxdenstudios.commons.time.ext

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone

/**
 * Returns the whole seconds since the Unix epoch of this date-time interpreted in [timeZone].
 *
 * Extensions on [LocalDateTime] for epoch conversion and formatting. They are `java.time`
 * extensions. The library needs no initialisation and targets minSdk 26+ or projects that
 * enable core library desugaring.
 *
 * Fractional seconds are floored, so pre-1970 values round toward negative infinity (one
 * millisecond before the epoch is -1, not 0). In a DST gap the time is shifted later by the
 * gap length; in an overlap the earlier offset is used.
 * Defaults to the JVM default zone ([TimeZone.getDefault]).
 */
fun LocalDateTime.toSeconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = toMilliseconds(timeZone).floorDiv(MILLIS_IN_SECOND)

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
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use LocalDateTime.format with DateTimeFormatter.ofPattern instead.",
    level = DeprecationLevel.WARNING,
)
fun LocalDateTime.toFormat(
    pattern: String = "dd MMM yyyy",
): String = format(DateTimeFormatter.ofPattern(pattern))

/** Formats the time of this date-time as 24-hour "HH:mm", e.g. "09:05". */
fun LocalDateTime.toHourMinutes(): String = format(DateTimeFormatter.ofPattern(HOUR_MINUTES_PATTERN))

private const val HOUR_MINUTES_PATTERN = "HH:mm"
private const val MILLIS_IN_SECOND = 1000L
