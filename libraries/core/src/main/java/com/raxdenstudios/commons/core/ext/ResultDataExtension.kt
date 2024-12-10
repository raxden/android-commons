package com.raxdenstudios.commons.core.ext

import com.raxdenstudios.commons.core.ResultData

@Suppress("TooGenericExceptionCaught")
fun <T, R> T.runCatching(
    function: T.() -> R
): ResultData<R, Throwable> = try {
    ResultData.Success(function())
} catch (e: Throwable) {
    ResultData.Failure(e)
}

fun <T, R, E> ResultData<T, E>.then(
    function: (value: T) -> R
): ResultData<R, E> = when (this) {
    is ResultData.Failure -> ResultData.Failure(value)
    is ResultData.Success -> ResultData.Success(function(value))
}

fun <T, R, E> ResultData<T, E>.thenFailure(
    function: (value: E) -> R
): ResultData<T, R> = when (this) {
    is ResultData.Failure -> ResultData.Failure(function(value))
    is ResultData.Success -> ResultData.Success(value)
}

fun <T, R, E> ResultData<T, E>.flatMap(
    function: (value: T) -> ResultData<R, E>
): ResultData<R, E> = when (this) {
    is ResultData.Failure -> ResultData.Failure(value)
    is ResultData.Success -> function(value)
}

fun <T, R, E> ResultData<T, E>.flatMapFailure(
    function: (value: E) -> ResultData<T, R>
): ResultData<T, R> = when (this) {
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

fun <T, E> ResultData<T, E>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (E) -> Unit
): Unit = when (this) {
    is ResultData.Failure -> onFailure(value)
    is ResultData.Success -> onSuccess(value)
}

fun <T, E> ResultData<T, E>.onSuccess(
    function: (success: T) -> Unit
): ResultData<T, E> = when (this) {
    is ResultData.Failure -> this
    is ResultData.Success -> also { function(value) }
}

fun <T, E> ResultData<T, E>.onFailure(
    function: (failure: E) -> Unit
): ResultData<T, E> = when (this) {
    is ResultData.Failure -> also { function(value) }
    is ResultData.Success -> this
}
