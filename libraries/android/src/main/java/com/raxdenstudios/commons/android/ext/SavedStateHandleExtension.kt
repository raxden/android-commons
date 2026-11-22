package com.raxdenstudios.commons.android.ext

import androidx.lifecycle.SavedStateHandle

/**
 * Returns the value stored under [key] as [T]. When the key is missing or the value is not a [T],
 * [onFailure] is called with an [IllegalStateException] and its result is returned.
 * Generic arguments are erased in the type check.
 */
inline fun <R, reified T : R> SavedStateHandle.getOrElse(
    key: String,
    onFailure: (exception: Throwable) -> R,
): R = runCatching { requireValue<T>(key) { "No value for key '$key'" } }.getOrElse(onFailure)

/**
 * Returns the value stored under [key] as [T], or [value] when the key is missing, holds `null`, or holds
 * a value that is not a [T]. Generic arguments are erased in the type check.
 */
inline fun <reified T : Any> SavedStateHandle.getOrDefault(
    key: String,
    value: T,
): T = get<Any?>(key) as? T ?: value

/**
 * Returns the value stored under [key] as [T].
 *
 * @throws IllegalStateException when the key is missing or holds `null`, or when the value is not a [T].
 * Generic arguments are erased in the type check.
 */
inline fun <reified T : Any> SavedStateHandle.getOrThrow(
    key: String,
): T = requireValue(key) { "No value for key '$key'" }

/**
 * Returns the value stored under [key] as [T].
 *
 * @throws IllegalStateException with [message] when the key is missing or holds `null`. A value that is
 * not a [T] fails with a generic message instead. Generic arguments are erased in the type check.
 */
inline fun <reified T : Any> SavedStateHandle.getOrThrow(
    key: String,
    message: String,
): T = requireValue(key) { message }

/**
 * Returns the value stored under [key] as [T], or `null` when the key is missing, holds `null`, or holds a
 * value that is not a [T]. Generic arguments are erased in the type check, so `List<String>` and
 * `List<Int>` are not told apart.
 */
inline fun <reified T : Any> SavedStateHandle.getOrNull(
    key: String,
): T? = get<Any?>(key) as? T

/**
 * Returns the value stored under [key] checked against [T], so a value of another type fails here
 * with an [IllegalStateException] instead of a `ClassCastException` at the call site.
 */
@PublishedApi
internal inline fun <reified T> SavedStateHandle.requireValue(
    key: String,
    missingMessage: () -> String,
): T {
    val value = checkNotNull(get<Any?>(key), missingMessage)
    check(value is T) { "Value for key '$key' has unexpected type ${value::class.qualifiedName}" }
    return value
}
