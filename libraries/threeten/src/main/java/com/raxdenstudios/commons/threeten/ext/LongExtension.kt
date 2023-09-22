package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.DateTimeUtils
import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import java.util.TimeZone

fun Long.toLocalDate(
    timeZone: TimeZone = TimeZone.getDefault()
): LocalDate = toLocalDateTime(timeZone).toLocalDate()

fun Long.toLocalDateTime(
    timeZone: TimeZone = TimeZone.getDefault()
): LocalDateTime = LocalDateTime.ofInstant(
    Instant.ofEpochMilli(this),
    DateTimeUtils.toZoneId(timeZone)
)
