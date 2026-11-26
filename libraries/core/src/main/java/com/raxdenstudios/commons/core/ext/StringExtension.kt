@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.core.ext

import java.text.DecimalFormat
import java.text.Normalizer
import java.text.NumberFormat
import java.text.ParsePosition
import java.util.Locale

fun String?.orDefault(
    default: String = String.EMPTY,
) = this ?: default

val String.Companion.EMPTY: String
    get() = ""

/**
 * Parses this string, trimmed, as a number written in the format of [locale], e.g. "1,234.5" in
 * [Locale.US] or "1.234,5" in [Locale.GERMANY] become 1234.5.
 *
 * The whole string must be a number, so "12abc" is rejected instead of read as 12.0. Grouping
 * separators are accepted only in complete groups ("1,234" but not "1,5" or "12,34" in
 * [Locale.US]), so a decimal comma typed in the wrong locale is not silently read as 15.0. A
 * rejected string falls back to Kotlin's [String.toDoubleOrNull] (which reads "1.5" or "1.5e2"),
 * and to 0.0 when that fails too.
 */
fun String.toDouble(
    locale: Locale = Locale.US,
): Double {
    val text = trim()
    return text.toLocalizedDoubleOrNull(locale) ?: text.toDoubleOrNull() ?: 0.0
}

private fun String.toLocalizedDoubleOrNull(
    locale: Locale,
): Double? {
    val format = NumberFormat.getInstance(locale)
    val position = ParsePosition(0)
    val number = format.parse(this, position)
    val isWholeNumber = position.index == length
    val hasValidGrouping = format !is DecimalFormat || hasValidGrouping(format)
    return number?.toDouble()?.takeIf { isWholeNumber && hasValidGrouping }
}

private fun String.hasValidGrouping(
    format: DecimalFormat,
): Boolean {
    val symbols = format.decimalFormatSymbols
    val integerPart = substringBefore(symbols.decimalSeparator).trimStart(symbols.minusSign, '+')
    val groups = integerPart.split(symbols.groupingSeparator)
    if (groups.size == 1) return true
    val groupingSize = format.groupingSize
    val usesGrouping = format.isGroupingUsed && groupingSize > 0
    return usesGrouping &&
        groups.first().length in 1..groupingSize &&
        groups.drop(1).all { group -> group.length == groupingSize }
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

/**
 * Upper-cases the first character of this string with the titlecase rules of [locale], and leaves the rest
 * unchanged, e.g. "hola" becomes "Hola" and "hOLA" becomes "HOLA". Replaces the deprecated `capitalize`.
 */
fun String.capitalized(
    locale: Locale = Locale.getDefault(),
): String = replaceFirstChar { char -> char.titlecase(locale) }

/**
 * Returns the name of the country for this ISO 3166-1 alpha-2 region code, written in [locale]. The code is
 * trimmed first, so "ES" becomes "Spain" in English or "España" in Spanish.
 *
 * Returns `null` when the code is invalid or blank, or when the platform has no name for it. In that last case
 * the platform would only echo the code back, so it is not treated as a name. Pair it with [toFlagEmoji] to show
 * a flag next to the name.
 */
fun String.toDisplayCountry(
    locale: Locale = Locale.getDefault(),
): String? = kotlin.runCatching { Locale.Builder().setRegion(trim()).build().getDisplayCountry(locale) }
    .getOrNull()
    ?.takeIf { name -> name.isNotBlank() && !name.equals(trim(), ignoreCase = true) }

private val DIACRITICS = Regex("""\p{Mn}+""")
private val NON_ALPHANUMERIC = Regex("""[^A-Za-z0-9]""")
private val WHITESPACES = Regex("""\s+""")
private const val ISO_COUNTRY_CODE_LENGTH = 2
private const val REGIONAL_INDICATOR_A = 0x1F1E6
