package com.raxdenstudios.commons.core.ext

import java.text.NumberFormat
import java.util.Locale

fun Double.toString(
    locale: Locale = Locale.getDefault(),
    minimumFractionDigits: Int = 3,
    maximumFractionDigits: Int = 3,
): String = NumberFormat.getNumberInstance(locale).apply {
    this.minimumFractionDigits = minimumFractionDigits
    this.maximumFractionDigits = maximumFractionDigits
}.format(this)