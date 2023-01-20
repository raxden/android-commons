package com.raxdenstudios.commons.ext

import androidx.lifecycle.SavedStateHandle

fun <R, T : R> SavedStateHandle.getOrElse(key: String, onFailure: (exception: Throwable) -> R): R =
    kotlin.runCatching { get<T>(key)!! }.getOrElse(onFailure)

fun <T : Any> SavedStateHandle.getOrDefault(key: String, value: T): T =
    kotlin.runCatching { get<T>(key)!! }.getOrDefault(value)

fun <T : Any> SavedStateHandle.getOrThrow(key: String): T =
    run { get<T>(key)!! }

fun <T : Any> SavedStateHandle.getOrThrow(key: String, message: String): T =
    get<T>(key) ?: error(message)

fun <T : Any> SavedStateHandle.getOrNull(key: String): T? =
    kotlin.runCatching { get<T>(key) }.getOrNull()
