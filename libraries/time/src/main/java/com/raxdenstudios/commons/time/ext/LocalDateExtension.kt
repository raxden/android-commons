package com.raxdenstudios.commons.time.ext

import java.time.LocalDate
import java.util.TimeZone

/**
 * Returns the whole seconds since the Unix epoch of the start of this date in [timeZone].
 *
 * `java.time` extensions on [LocalDate] for epoch conversion. They need no initialisation and target
 * minSdk 26+ or projects that enable core library desugaring.
 *
 * The start of day is the earliest valid time on this date in that zone. Fractional
 * seconds are floored, so pre-1970 values round toward negative infinity (one millisecond
 * before the epoch is -1, not 0). Defaults to the JVM default zone ([TimeZone.getDefault]).
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
 * ```
 */
fun LocalDate.toSeconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = toMilliseconds(timeZone).floorDiv(MILLIS_IN_SECOND)

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

private const val MILLIS_IN_SECOND = 1000L
