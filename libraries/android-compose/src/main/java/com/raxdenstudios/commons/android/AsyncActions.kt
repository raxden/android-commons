package com.raxdenstudios.commons.android

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Returns a callback that launches [block] in the composition's coroutine scope each time it is invoked.
 *
 * The returned lambda keeps the same instance across recompositions (it only changes if the composition's
 * scope does), so passing it to a child composable does not prevent that child from skipping. Each
 * invocation runs the [block] from the latest recomposition.
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use rememberCoroutineScope and launch directly.",
    level = DeprecationLevel.WARNING,
)
@Composable
fun runAsync(
    block: suspend CoroutineScope.() -> Unit,
): () -> Unit {
    val scope = rememberCoroutineScope()
    val currentBlock by rememberUpdatedState(block)
    return remember(scope) { { scope.launch { currentBlock() } } }
}

/**
 * Returns a callback that launches [block] with the given value in the composition's coroutine scope each
 * time it is invoked. Same instance across recompositions; each invocation runs the latest [block].
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use rememberCoroutineScope and launch directly.",
    level = DeprecationLevel.WARNING,
)
@Composable
fun <T> consumeAsync(
    block: suspend CoroutineScope.(T) -> Unit,
): (T) -> Unit {
    val scope = rememberCoroutineScope()
    val currentBlock by rememberUpdatedState(block)
    return remember(scope) { { value -> scope.launch { currentBlock(value) } } }
}

/**
 * Returns a callback that launches [block] with the given values in the composition's coroutine scope each
 * time it is invoked. Same instance across recompositions; each invocation runs the latest [block].
 */
@Deprecated(
    message = "Will be removed in the next major version. " +
        "Use rememberCoroutineScope and launch directly.",
    level = DeprecationLevel.WARNING,
)
@Composable
fun <T, R> consumeAsync(
    block: suspend CoroutineScope.(T, R) -> Unit,
): (T, R) -> Unit {
    val scope = rememberCoroutineScope()
    val currentBlock by rememberUpdatedState(block)
    return remember(scope) { { param1, param2 -> scope.launch { currentBlock(param1, param2) } } }
}
