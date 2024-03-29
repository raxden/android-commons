package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun String.toLocalDate(vararg patterns: String): LocalDate? {
    patterns.forEach { format ->
        val dateTimeFormatter = DateTimeFormatter.ofPattern(format)
        toLocalDate(dateTimeFormatter)?.also { localDate -> return localDate }
    }
    return null
}

fun String.toLocalDate(vararg formatters: DateTimeFormatter): LocalDate? {
    formatters.forEach { dateTimeFormatter ->
        runCatching { LocalDate.parse(this, dateTimeFormatter) }
            .getOrNull()?.also { localDate -> return localDate }
    }
    return null
}
