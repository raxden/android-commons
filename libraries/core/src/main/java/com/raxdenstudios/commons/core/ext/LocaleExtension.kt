package com.raxdenstudios.commons.core.ext

import java.util.Locale

/**
 * Returns the name of this locale's language, written in [inLocale], e.g. "Spanish" for `es-ES` in English.
 *
 * The text is exactly what the JDK returns, so its capitalisation follows [inLocale]'s conventions
 * (some languages are lowercase in their own language). Returns the language code when no name is known.
 */
fun Locale.displayLanguageName(
    inLocale: Locale = Locale.getDefault(),
): String = getDisplayLanguage(inLocale)
