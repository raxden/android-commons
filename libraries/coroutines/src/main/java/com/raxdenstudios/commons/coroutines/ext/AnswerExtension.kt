@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.coroutines.ext

import com.raxdenstudios.commons.core.Answer
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Runs [function] and wraps its result in [Answer.Success], or any thrown error in [Answer.Failure].
 *
 * A [CancellationException] is rethrown instead of being wrapped, so the calling coroutine is
 * cancelled as expected.
 */
@Suppress("TooGenericExceptionCaught")
suspend fun <T, R> T.coRunCatching(
    function: suspend T.() -> R,
): Answer<R, Throwable> = try {
    Answer.Success(function())
} catch (e: CancellationException) {
    throw e
} catch (e: Throwable) {
    Answer.Failure(e)
}

suspend fun <T, R, E> Answer<T, E>.coThen(
    function: suspend (value: T) -> R,
): Answer<R, E> = when (this) {
    is Answer.Failure -> Answer.Failure(value)
    is Answer.Success -> Answer.Success(function(value))
}

suspend fun <T, R, E> Answer<T, E>.coThenFailure(
    function: suspend (value: E) -> R,
): Answer<T, R> = when (this) {
    is Answer.Failure -> Answer.Failure(function(value))
    is Answer.Success -> Answer.Success(value)
}

suspend fun <T, R, E> Answer<T, E>.coFlatMap(
    function: suspend (value: T) -> Answer<R, E>,
): Answer<R, E> = when (this) {
    is Answer.Failure -> Answer.Failure(value)
    is Answer.Success -> function(value)
}

suspend fun <T, E> Answer<T, E>.onCoSuccess(
    function: suspend (success: T) -> Unit,
): Answer<T, E> = when (this) {
    is Answer.Failure -> this
    is Answer.Success -> also { function(value) }
}

suspend fun <T, E> Answer<T, E>.onCoFailure(
    function: suspend (failure: E) -> Unit,
): Answer<T, E> = when (this) {
    is Answer.Failure -> also { function(value) }
    is Answer.Success -> this
}

fun <T, R, E> Flow<Answer<T, E>>.then(
    function: suspend (value: T) -> R,
): Flow<Answer<R, E>> = map { result -> result.coThen { function(it) } }

fun <T, R, E> Flow<Answer<T, E>>.thenFailure(
    function: (value: E) -> R,
): Flow<Answer<T, R>> = map { result -> result.coThenFailure { function(it) } }

fun <T> Flow<T>.toAnswer(): Flow<Answer<T, Throwable>> = toAnswer { error -> error }

fun <T, E> Flow<T>.toAnswer(
    mapFailure: (Throwable) -> E,
): Flow<Answer<T, E>> {
    val answers: Flow<Answer<T, E>> = map { data -> Answer.Success(data) }
    return answers.catch { error -> emit(Answer.Failure(mapFailure(error))) }
}
