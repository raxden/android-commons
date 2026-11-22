package com.raxdenstudios.commons.time.ext

import java.time.LocalDateTime
import java.util.TimeZone

/**
 * Returns the milliseconds since the Unix epoch of this date-time interpreted in [timeZone].
 *
 * Extension on [LocalDateTime] for epoch conversion. It is a `java.time` extension. The library needs
 * no initialisation and targets minSdk 26+ or projects that enable core library desugaring.
 *
 * In a DST gap the time is shifted later by the gap length; in an overlap the earlier offset is used.
 * Defaults to the JVM default zone ([TimeZone.getDefault]).
 */
fun LocalDateTime.toMilliseconds(
    timeZone: TimeZone = TimeZone.getDefault(),
): Long = atZone(timeZone.toZoneId()).toInstant().toEpochMilli()
