package com.raxdenstudios.commons.android.ext

import androidx.lifecycle.SavedStateHandle

fun <T : Any> SavedStateHandle.getOrThrow(
    key: String,
): T = run { get<T>(key)!! }

fun <T : Any> SavedStateHandle.getOrThrow(
    key: String,
    message: String,
): T = get<T>(key) ?: error(message)
