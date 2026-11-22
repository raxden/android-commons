package com.raxdenstudios.commons.core.ext

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

fun Double.toString(
    locale: Locale = Locale.getDefault(),
    minimumFractionDigits: Int = 3,
    maximumFractionDigits: Int = 3,
): String = NumberFormat.getNumberInstance(locale).apply {
    this.minimumFractionDigits = minimumFractionDigits
    this.maximumFractionDigits = maximumFractionDigits
}.format(this)

/**
 * Formats this amount as money in [currency] with the grouping and symbol style of [locale], e.g. "$1,234.50"
 * for USD in en-US. The number of decimal digits comes from [currency].
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use NumberFormat.getCurrencyInstance instead.",
    level = DeprecationLevel.WARNING,
)
fun Double.toCurrencyString(
    currency: Currency,
    locale: Locale = Locale.getDefault(),
): String = NumberFormat.getCurrencyInstance(locale).apply {
    this.currency = currency
}.format(this)
