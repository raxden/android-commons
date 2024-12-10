@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.coroutines.ext

import com.raxdenstudios.commons.core.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Suppress("TooGenericExceptionCaught")
suspend fun <T, R> T.coRunCatching(
    function: suspend T.() -> R
): ResultData<R, Throwable> = try {
    ResultData.Success(function())
} catch (e: Throwable) {
    ResultData.Failure(e)
}

suspend fun <T, R, E> ResultData<T, E>.coThen(
    function: suspend (value: T) -> R
): ResultData<R, E> = when (this) {
    is ResultData.Failure -> ResultData.Failure(value)
    is ResultData.Success -> ResultData.Success(function(value))
}

suspend fun <T, R, E> ResultData<T, E>.coThenFailure(
    function: suspend (value: E) -> R
): ResultData<T, R> = when (this) {
    is ResultData.Failure -> ResultData.Failure(function(value))
    is ResultData.Success -> ResultData.Success(value)
}

suspend fun <T, R, E> ResultData<T, E>.coFlatMap(
    function: suspend (value: T) -> ResultData<R, E>
): ResultData<R, E> = when (this) {
    is ResultData.Failure -> ResultData.Failure(value)
    is ResultData.Success -> function(value)
}

suspend fun <T, R, E> ResultData<T, E>.coFlatMapFailure(
    function: suspend (value: E) -> ResultData<T, R>
): ResultData<T, R> = when (this) {
    is ResultData.Failure -> function(value)
    is ResultData.Success -> ResultData.Success(value)
}

suspend fun <T, E> ResultData<T, E>.onCoSuccess(
    function: suspend (success: T) -> Unit
): ResultData<T, E> = when (this) {
    is ResultData.Failure -> this
    is ResultData.Success -> also { function(value) }
}

suspend fun <T, E> ResultData<T, E>.onCoFailure(
    function: suspend (failure: E) -> Unit
): ResultData<T, E> = when (this) {
    is ResultData.Failure -> also { function(value) }
    is ResultData.Success -> this
}

fun <T, R, E> Flow<ResultData<T, E>>.then(
    function: suspend (value: T) -> R
): Flow<ResultData<R, E>> =
    map { result -> result.coThen { function(it) } }

fun <T, R, E> Flow<ResultData<T, E>>.thenFailure(
    function: (value: E) -> R
): Flow<ResultData<T, R>> =
    map { result -> result.coThenFailure { function(it) } }
