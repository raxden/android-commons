package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.TimeZone

fun LocalDateTime.toSeconds(
    timeZone: TimeZone = TimeZone.getDefault()
): Long = toMilliseconds(timeZone) / MILLIS_IN_SECOND

fun LocalDateTime.toMilliseconds(
    timeZone: TimeZone = TimeZone.getDefault()
): Long = atZone(DateTimeUtils.toZoneId(timeZone)).toInstant().toEpochMilli()

fun LocalDateTime.toFormat(
    pattern: String = "dd MMM yyyy"
): String = format(DateTimeFormatter.ofPattern(pattern))

private const val MILLIS_IN_SECOND = 1000L
