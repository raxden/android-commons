@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.ext

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

fun String?.orDefault(default: String = String.EMPTY) = this ?: default

fun String?.ifEmptyOrNullThen(text: String): String =
    this?.ifEmpty { text } ?: text

val String.Companion.EMPTY: String
    get() = ""

fun String.ifEmptyThen(text: String): String {
    return this.ifEmpty { text }
}

@Suppress("MagicNumber")
fun String.toMD5(): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(toByteArray())).toString(16).padStart(32, '0')
}

fun String.toSHA256(charset: Charset = Charsets.UTF_8): String =
    hash(this, "SHA-256", charset)

fun String.toSHA512(charset: Charset = Charsets.UTF_8): String =
    hash(this, "SHA-512", charset)

private fun hash(
    key: String,
    algorithm: String,
    charset: Charset,
): String = digest(key, algorithm, charset)
    ?.let { bytesToHexString(it) } ?: ""

private fun digest(
    key: String,
    algorithm: String,
    charset: Charset = Charsets.UTF_8,
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
