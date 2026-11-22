@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.core.ext

import java.text.Normalizer
import java.text.NumberFormat
import java.text.ParseException
import java.util.Locale

fun String?.orDefault(
    default: String = String.EMPTY,
) = this ?: default

fun String?.ifEmptyOrNullThen(
    text: String,
): String = this?.ifEmpty { text } ?: text

val String.Companion.EMPTY: String
    get() = ""

fun String.ifEmptyThen(
    text: String,
): String = this.ifEmpty { text }

fun String.toDouble(
    locale: Locale = Locale.US,
): Double = try {
    NumberFormat.getInstance(locale)
        .parse(trim())?.toDouble() ?: 0.0
} catch (_: ParseException) {
    toDoubleOrNull() ?: 0.0
}

/**
 * Removes diacritical marks by decomposing the text (NFD) and dropping the combining marks, e.g. "Canción"
 * becomes "Cancion". Letters without a decomposition, such as "ß" or "ø", are left unchanged.
 */
fun String.removeDiacritics(): String =
    Normalizer.normalize(this, Normalizer.Form.NFD).replace(DIACRITICS, String.EMPTY)

/**
 * Keeps only the ASCII letters (`A-Z`, `a-z`) and digits of this string, e.g. "Rock & Roll 80's" becomes
 * "RockRoll80s". Non-ASCII letters are removed too, so call [removeDiacritics] first to keep "Canción" as "Cancion".
 */
fun String.alphanumericOnly(): String = replace(NON_ALPHANUMERIC, String.EMPTY)

/**
 * Trims this string and replaces every run of whitespace (spaces, tabs, line breaks) with a single space,
 * e.g. " Hello \n  world " becomes "Hello world".
 */
fun String.collapseWhitespace(): String = replace(WHITESPACES, " ").trim()

/**
 * Returns the flag emoji for this ISO 3166-1 alpha-2 country code, e.g. "es" becomes the Spanish flag.
 *
 * The code is trimmed and case-insensitive. Returns an empty string (not an error) when the input is not
 * exactly two ASCII letters, e.g. "", "E", "ESP" or "1A", so check the result before showing it.
 */
fun String.toFlagEmoji(): String {
    val iso = trim().uppercase(Locale.ROOT)
    if (iso.length != ISO_COUNTRY_CODE_LENGTH || iso.any { char -> char !in 'A'..'Z' }) return String.EMPTY
    return iso.map { char -> REGIONAL_INDICATOR_A + (char.code - 'A'.code) }
        .joinToString(String.EMPTY) { codePoint -> String(Character.toChars(codePoint)) }
}

private val DIACRITICS = Regex("""\p{Mn}+""")
private val NON_ALPHANUMERIC = Regex("""[^A-Za-z0-9]""")
private val WHITESPACES = Regex("""\s+""")
private const val ISO_COUNTRY_CODE_LENGTH = 2
private const val REGIONAL_INDICATOR_A = 0x1F1E6
