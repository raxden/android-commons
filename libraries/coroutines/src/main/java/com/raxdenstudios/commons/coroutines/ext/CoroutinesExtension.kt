package com.raxdenstudios.commons.coroutines.ext

import com.raxdenstudios.commons.coroutines.CoroutineLogger
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

var defaultCoroutineLogger: CoroutineLogger? = null
val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    defaultCoroutineLogger?.logError("CoroutineException", throwable.message, throwable)
}

fun CoroutineScope.safeLaunch(
    exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = exceptionHandler,
    block = block
)

fun CoroutineScope.launch(
    onError: (Throwable) -> Unit = {},
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = CoroutineExceptionHandler { _, throwable ->
        defaultCoroutineLogger?.logError("CoroutineException", throwable.message, throwable)
        onError(throwable)
    },
    block = block
)
