package com.raxdenstudios.commons.time.ext

import java.time.DayOfWeek
import java.time.format.TextStyle
import java.util.Locale

/**
 * Returns the abbreviated name of this day in [locale], e.g. "Mon" in English. Uses the default device
 * locale unless [locale] is given.
 */
fun DayOfWeek.shortName(
    locale: Locale = Locale.getDefault(),
): String = getDisplayName(TextStyle.SHORT, locale)

/**
 * Returns the shortest name of this day in [locale], e.g. "M" for Monday in English. Narrow names are not
 * always unique (Tuesday and Thursday are both "T" in English), so do not use them as keys. Uses the default
 * device locale unless [locale] is given.
 */
fun DayOfWeek.narrowName(
    locale: Locale = Locale.getDefault(),
): String = getDisplayName(TextStyle.NARROW, locale)
