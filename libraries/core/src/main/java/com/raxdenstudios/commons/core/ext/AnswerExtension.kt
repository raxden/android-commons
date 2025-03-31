package com.raxdenstudios.commons.core.ext

import com.raxdenstudios.commons.core.Answer

@Suppress("TooGenericExceptionCaught")
fun <T, R> T.runCatching(
    function: T.() -> R
): Answer<R, Throwable> = try {
    Answer.Success(function())
} catch (e: Throwable) {
    Answer.Failure(e)
}

fun <T, R, E> Answer<T, E>.then(
    function: (value: T) -> R
): Answer<R, E> = when (this) {
    is Answer.Failure -> Answer.Failure(value)
    is Answer.Success -> Answer.Success(function(value))
}

fun <T, R, E> Answer<T, E>.thenFailure(
    function: (value: E) -> R
): Answer<T, R> = when (this) {
    is Answer.Failure -> Answer.Failure(function(value))
    is Answer.Success -> Answer.Success(value)
}

fun <T, R, E> Answer<T, E>.flatMap(
    function: (value: T) -> Answer<R, E>
): Answer<R, E> = when (this) {
    is Answer.Failure -> Answer.Failure(value)
    is Answer.Success -> function(value)
}

fun <T, R, E> Answer<T, E>.flatMapFailure(
    function: (value: E) -> Answer<T, R>
): Answer<T, R> = when (this) {
    is Answer.Failure -> function(value)
    is Answer.Success -> Answer.Success(value)
}

fun <T, E> Answer<T, E>.getValueOrNull(): T? = when (this) {
    is Answer.Failure -> null
    is Answer.Success -> value
}

fun <T, E> Answer<T, E>.getValueOrDefault(default: T): T = when (this) {
    is Answer.Failure -> default
    is Answer.Success -> value
}

fun <T, E> Answer<T, E>.fold(
    onSuccess: (T) -> Unit,
    onFailure: (E) -> Unit
): Unit = when (this) {
    is Answer.Failure -> onFailure(value)
    is Answer.Success -> onSuccess(value)
}

fun <T, E> Answer<T, E>.onSuccess(
    function: (success: T) -> Unit
): Answer<T, E> = when (this) {
    is Answer.Failure -> this
    is Answer.Success -> also { function(value) }
}

fun <T, E> Answer<T, E>.onFailure(
    function: (failure: E) -> Unit
): Answer<T, E> = when (this) {
    is Answer.Failure -> also { function(value) }
    is Answer.Success -> this
}
