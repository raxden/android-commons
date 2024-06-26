package com.raxdenstudios.commons.coroutines.ext

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

val defaultExceptionHandler = CoroutineExceptionHandler { _, throwable ->
    Log.e("CoroutineException", throwable.message, throwable)
}

fun CoroutineScope.safeLaunch(
    exceptionHandler: CoroutineExceptionHandler = defaultExceptionHandler,
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = exceptionHandler,
    block = { block.invoke(this) }
)

fun CoroutineScope.launch(
    onError: (throwable: Throwable) -> Unit = { _ -> },
    block: suspend CoroutineScope.() -> Unit,
): Job = launch(
    context = CoroutineExceptionHandler { _, throwable ->
        Log.e("CoroutineException", throwable.message, throwable)
        onError(throwable)
    },
    block = { block.invoke(this) }
)
