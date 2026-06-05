@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.core.ext

import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

fun String?.orDefault(default: String = String.EMPTY) = this ?: default

fun String?.ifEmptyOrNullThen(text: String): String =
    this?.ifEmpty { text } ?: text

val String.Companion.EMPTY: String
    get() = ""

fun String.ifEmptyThen(text: String): String {
    return this.ifEmpty { text }
}

fun String.toDouble(locale: Locale = Locale.getDefault()): Double = try {
    NumberFormat.getInstance(locale)
        .parse(trim())?.toDouble() ?: 0.0
} catch (_: ParseException) {
    toDoubleOrNull() ?: 0.0
}
