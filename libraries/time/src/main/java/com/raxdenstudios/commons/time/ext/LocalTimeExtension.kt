package com.raxdenstudios.commons.time.ext

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

/**
 * Returns this time as localized text in the [FormatStyle.SHORT] style, e.g. "09:05" in en-GB or "9:05 AM" in
 * en-US. Uses the default device locale unless [locale] is given.
 */
fun LocalTime.toShortText(
    locale: Locale = Locale.getDefault(),
): String = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale).format(this)
