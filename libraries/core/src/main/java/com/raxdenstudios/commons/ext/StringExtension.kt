@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.ext

import android.text.Spanned
import android.util.Base64
import android.util.Patterns
import androidx.core.text.HtmlCompat
import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

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

fun String.ifEmptyThen(text: String): String {
    return this.ifEmpty { text }
}

@Suppress("MagicNumber")
fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun String.toSHA256(charset: Charset = Charsets.UTF_8): String {
    return hash(this, "SHA-256", charset)
}

fun String.toSHA512(charset: Charset = Charsets.UTF_8): String {
    return hash(this, "SHA-512", charset)
}

private fun hash(key: String, algorithm: String, charset: Charset): String {
    return digest(key, algorithm, charset)?.let { bytesToHexString(it) } ?: ""
}

private fun digest(
    key: String,
    algorithm: String,
    charset: Charset = Charsets.UTF_8
): ByteArray? = runCatching {
    val messageDigest = MessageDigest.getInstance(algorithm)
    messageDigest.update(key.toByteArray(charset))
    messageDigest.digest()
}.getOrNull()

@Suppress("MagicNumber")
private fun bytesToHexString(bytes: ByteArray): String {
    // http://stackoverflow.com/questions/332079
    val sb = StringBuilder()
    for (i in bytes.indices) {
        val hex = Integer.toHexString(0xFF and bytes[i].toInt())
        if (hex.length == 1) {
            sb.append('0')
        }
        sb.append(hex)
    }
    return sb.toString()
}
