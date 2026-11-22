@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.android.ext

import android.text.Spanned
import android.util.Base64
import android.util.Patterns
import androidx.core.text.HtmlCompat
import java.nio.charset.Charset

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Patterns, HtmlCompat and Base64 directly.",
    level = DeprecationLevel.WARNING,
)
fun String.isUrl() = Patterns.WEB_URL.matcher(this).matches()

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Patterns, HtmlCompat and Base64 directly.",
    level = DeprecationLevel.WARNING,
)
fun String.isPhone() = Patterns.PHONE.matcher(this).matches()

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Patterns, HtmlCompat and Base64 directly.",
    level = DeprecationLevel.WARNING,
)
fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Patterns, HtmlCompat and Base64 directly.",
    level = DeprecationLevel.WARNING,
)
fun String.toHtml(): Spanned = HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Patterns, HtmlCompat and Base64 directly.",
    level = DeprecationLevel.WARNING,
)
fun String.encodeToBase64(
    charset: Charset = Charsets.UTF_8,
    flags: Int = Base64.NO_WRAP,
): String = run { Base64.encodeToString(toByteArray(charset), flags) }

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use Patterns, HtmlCompat and Base64 directly.",
    level = DeprecationLevel.WARNING,
)
fun String.decodeFromBase64(
    charset: Charset = Charsets.UTF_8,
    flags: Int = Base64.NO_WRAP,
): String = run { String(Base64.decode(this, flags), charset) }
