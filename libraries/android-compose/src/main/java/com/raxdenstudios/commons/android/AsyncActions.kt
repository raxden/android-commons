package com.raxdenstudios.commons.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun runAsync(
    block: suspend CoroutineScope.() -> Unit,
): () -> Unit {
    val scope = rememberCoroutineScope()
    return { scope.launch { block() } }
}

@Composable
fun <T> consumeAsync(
    block: suspend CoroutineScope.(T) -> Unit,
): (T) -> Unit {
    val scope = rememberCoroutineScope()
    return { scope.launch { block(it) } }
}

@Composable
fun <T, R> consumeAsync(
    block: suspend CoroutineScope.(T, R) -> Unit,
): (T, R) -> Unit {
    val scope = rememberCoroutineScope()
    return { param1, param2 -> scope.launch { block(param1, param2) } }
}
