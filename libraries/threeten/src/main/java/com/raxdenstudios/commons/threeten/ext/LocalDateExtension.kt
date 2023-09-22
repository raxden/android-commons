package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.TimeZone

fun LocalDate.toSeconds(
    timeZone: TimeZone = TimeZone.getDefault()
): Long = toMilliseconds(timeZone) / MILLIS_IN_SECOND

fun LocalDate.toMilliseconds(
    timeZone: TimeZone = TimeZone.getDefault()
): Long = atStartOfDay().atZone(DateTimeUtils.toZoneId(timeZone)).toInstant().toEpochMilli()

fun LocalDate.isToday(): Boolean = this == LocalDate.now()

fun LocalDate.toFormat(
    pattern: String = "dd MMM yyyy"
): String = format(DateTimeFormatter.ofPattern(pattern))

private const val MILLIS_IN_SECOND = 1000L
