package com.raxdenstudios.commons.coroutines.ext

import com.raxdenstudios.commons.coroutines.CoroutineLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Logger used by [defaultExceptionHandler], [safeLaunch] and [launch] to report uncaught coroutine errors.
 * Usually set once at app start. It is volatile, so a value set on one thread is seen by coroutines failing
 * on any other.
 */
@Volatile
var defaultCoroutineLogger: CoroutineLogger? = null
val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    defaultCoroutineLogger?.logError("CoroutineException", throwable.message, throwable)
}

fun CoroutineScope.safeLaunch(
    exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = exceptionHandler,
    block = block,
)

fun CoroutineScope.launch(
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = CoroutineExceptionHandler { _, throwable ->
        defaultCoroutineLogger?.logError("CoroutineException", throwable.message, throwable)
        onError(throwable)
    },
    block = block,
)
