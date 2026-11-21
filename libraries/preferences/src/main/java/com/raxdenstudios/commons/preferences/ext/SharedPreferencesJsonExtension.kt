package com.raxdenstudios.commons.preferences.ext

import android.content.SharedPreferences
import kotlinx.serialization.json.Json

/**
 * Decodes the JSON stored under [key] into [T], or returns null when the key is missing or the
 * stored text cannot be decoded into [T].
 *
 * Decoding errors are swallowed, so a corrupt or outdated value looks the same as a missing one.
 * [T] must be `@Serializable` and the calling module must apply the kotlinx serialization plugin.
 * Pass a custom [json] to change the decoding configuration; the default is [Json].
 *
 * ## Usage
 *
 * ```kotlin
 * @Serializable
 * data class Session(val token: String, val expiresAt: Long)
 *
 * val session: Session? = preferences.getJson("session")
 * ```
 *
 * @param key the preference key to read.
 * @param json the serializer configuration used to decode the value.
 */
inline fun <reified T> SharedPreferences.getJson(
    key: String,
    json: Json = Json,
): T? {
    val raw = runCatching { getString(key, null) }.getOrNull() ?: return null
    return runCatching { json.decodeFromString<T>(raw) }.getOrNull()
}

/** Stores [value] under [key] encoded as JSON. */
inline fun <reified T> SharedPreferences.Editor.putJson(
    key: String,
    value: T,
    json: Json = Json,
): SharedPreferences.Editor = putString(key, json.encodeToString(value))

/** Stores [value] under [key], or removes the key when [value] is null. */
fun SharedPreferences.Editor.putOrRemove(
    key: String,
    value: String?,
): SharedPreferences.Editor = if (value == null) remove(key) else putString(key, value)
