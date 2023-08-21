@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.ext

import android.text.Spanned
import android.util.Base64
import android.util.Patterns
import androidx.core.text.HtmlCompat
import java.nio.charset.Charset

fun String.isUrl() = Patterns.WEB_URL.matcher(this).matches()

fun String.isPhone() = Patterns.PHONE.matcher(this).matches()

fun String.isEmail() = Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String.toHtml(): Spanned =
    HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_COMPACT)

fun String.encodeToBase64(
    charset: Charset = Charsets.UTF_8,
    flags: Int = Base64.NO_WRAP
): String = run { Base64.encodeToString(toByteArray(charset), flags) }

fun String.decodeFromBase64(
    charset: Charset = Charsets.UTF_8,
    flags: Int = Base64.NO_WRAP
): String = run { String(Base64.decode(this, flags), charset) }
