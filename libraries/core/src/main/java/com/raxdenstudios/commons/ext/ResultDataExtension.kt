@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.ext

import com.raxdenstudios.commons.ResultData

@Suppress("TooGenericExceptionCaught")
inline fun <T, R> T.runCatching(block: T.() -> R): ResultData<R, Throwable> =
    try {
        ResultData.Success(block())
    } catch (e: Throwable) {
        ResultData.Failure(e)
    }

fun <T, R, E> ResultData<T, E>.map(function: (value: T) -> R): ResultData<R, E> =
    when (this) {
        is ResultData.Failure -> ResultData.Failure(value)
        is ResultData.Success -> ResultData.Success(function(value))
    }

fun <T, R, E> ResultData<T, E>.mapFailure(function: (value: E) -> R): ResultData<T, R> =
    when (this) {
        is ResultData.Failure -> ResultData.Failure(function(value))
        is ResultData.Success -> ResultData.Success(value)
    }

suspend fun <T, R, E> ResultData<T, E>.coMap(function: suspend (value: T) -> R): ResultData<R, E> =
    when (this) {
        is ResultData.Failure -> ResultData.Failure(value)
        is ResultData.Success -> ResultData.Success(function(value))
    }

suspend fun <T, R, E> ResultData<T, E>.coMapFailure(function: suspend (value: E) -> R): ResultData<T, R> =
    when (this) {
        is ResultData.Failure -> ResultData.Failure(function(value))
        is ResultData.Success -> ResultData.Success(value)
    }

fun <T, R, E> ResultData<T, E>.flatMap(function: (value: T) -> ResultData<R, E>): ResultData<R, E> =
    when (this) {
        is ResultData.Failure -> ResultData.Failure(value)
        is ResultData.Success -> function(value)
    }

fun <T, R, E> ResultData<T, E>.flatMapFailure(function: (value: E) -> ResultData<T, R>): ResultData<T, R> =
    when (this) {
        is ResultData.Failure -> function(value)
        is ResultData.Success -> ResultData.Success(value)
    }

suspend fun <T, R, E> ResultData<T, E>.coFlatMap(function: suspend (value: T) -> ResultData<R, E>): ResultData<R, E> =
    when (this) {
        is ResultData.Failure -> ResultData.Failure(value)
        is ResultData.Success -> function(value)
    }

suspend fun <T, R, E> ResultData<T, E>.coFlatMapFailure(
    function: suspend (value: E) -> ResultData<T, R>
): ResultData<T, R> =
    when (this) {
        is ResultData.Failure -> function(value)
        is ResultData.Success -> ResultData.Success(value)
    }

fun <T, E> ResultData<T, E>.getValueOrNull(): T? = when (this) {
    is ResultData.Failure -> null
    is ResultData.Success -> value
}

fun <T, E> ResultData<T, E>.getValueOrDefault(default: T): T = when (this) {
    is ResultData.Failure -> default
    is ResultData.Success -> value
}

fun <T, E> ResultData<T, E>.fold(onSuccess: (T) -> Unit, onFailure: (E) -> Unit): Unit =
    when (this) {
        is ResultData.Failure -> onFailure(value)
        is ResultData.Success -> onSuccess(value)
    }

fun <T, E> ResultData<T, E>.onSuccess(function: (success: T) -> Unit): ResultData<T, E> =
    when (this) {
        is ResultData.Failure -> this
        is ResultData.Success -> also { function(value) }
    }

suspend fun <T, E> ResultData<T, E>.onCoSuccess(function: suspend (success: T) -> Unit): ResultData<T, E> =
    when (this) {
        is ResultData.Failure -> this
        is ResultData.Success -> also { function(value) }
    }

fun <T, E> ResultData<T, E>.onFailure(function: (failure: E) -> Unit): ResultData<T, E> =
    when (this) {
        is ResultData.Failure -> also { function(value) }
        is ResultData.Success -> this
    }

suspend fun <T, E> ResultData<T, E>.onCoFailure(function: suspend (failure: E) -> Unit): ResultData<T, E> =
    when (this) {
        is ResultData.Failure -> also { function(value) }
        is ResultData.Success -> this
    }
