package com.raxdenstudios.commons.ext

import androidx.lifecycle.SavedStateHandle

fun <R, T : R> SavedStateHandle.getOrElse(key: String, onFailure: (exception: Throwable) -> R) =
  runCatching { get<T>(key) }.getOrElse(onFailure)

fun <T : Any> SavedStateHandle.getOrDefault(key: String, value: T) =
  runCatching { get<T>(key) }.getOrDefault(value)

fun <T : Any> SavedStateHandle.getOrThrow(key: String) =
  run { get<T>(key) }

fun <T : Any> SavedStateHandle.getOrThrow(key: String, message: String) =
  get<T>(key) ?: error(message)

fun <T : Any> SavedStateHandle.getOrNull(key: String): T? =
  runCatching { get<T>(key) }.getOrNull()
