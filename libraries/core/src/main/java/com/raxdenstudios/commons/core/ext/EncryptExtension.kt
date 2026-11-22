package com.raxdenstudios.commons.core.ext

import java.nio.charset.Charset
import java.security.MessageDigest

fun String.toSHA256(
    charset: Charset = Charsets.UTF_8,
): String = hash(this, "SHA-256", charset)

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
private fun bytesToHexString(
    bytes: ByteArray,
): String {
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
