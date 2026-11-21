package com.raxdenstudios.commons.time.ext

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.TimeZone

/**
 * Converts this epoch-milliseconds value to a [LocalDate] in [timeZone].
 *
 * Extensions on [Long] that convert epoch milliseconds to java.time values. The receiver is
 * always epoch milliseconds, not seconds. They are `java.time` extensions. The library needs
 * no initialisation and targets minSdk 26+ or projects that enable core library desugaring.
 *
 * Defaults to the JVM default zone ([TimeZone.getDefault]).
 */
fun Long.toLocalDate(
    timeZone: TimeZone = TimeZone.getDefault(),
): LocalDate = toLocalDateTime(timeZone).toLocalDate()

/**
 * Converts this epoch-milliseconds value to a [LocalDateTime] in [timeZone].
 *
 * Defaults to the JVM default zone ([TimeZone.getDefault]).
 */
fun Long.toLocalDateTime(
    timeZone: TimeZone = TimeZone.getDefault(),
): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this),
    timeZone.toZoneId(),
)
