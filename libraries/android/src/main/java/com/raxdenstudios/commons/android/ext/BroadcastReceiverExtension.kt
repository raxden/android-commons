package com.raxdenstudios.commons.android.ext

import android.content.BroadcastReceiver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration

/**
 * Runs the suspend [block] in [scope] for a broadcast, keeping the receiver alive until it ends.
 *
 * Call it synchronously from `onReceive`: it calls the platform `goAsync()` and launches [block]
 * in [scope]. The platform `PendingResult` is finished when the launched [Job] completes, however
 * it ends: the block returns, throws, is cancelled, or times out. Outside a broadcast, `goAsync()`
 * returns `null`; the block still runs and nothing is finished. An exception from [block] is not
 * caught here. It reaches the exception handling of [scope], so catch it inside [block] when it
 * must not fail the scope.
 *
 * With a [timeout], [block] is cancelled once that time passes and [onTimeout] is called inside the
 * job, so an exception thrown by [onTimeout] also reaches [scope]. A `null` timeout, or
 * `Duration.INFINITE`, means no limit. Keep the work well under the roughly 10 seconds the system
 * allows a broadcast receiver. The result is finished even when [scope] is cancelled before the job
 * starts; in that case [block] never runs.
 *
 * Returns the [Job] of the launched coroutine, which can be cancelled or joined.
 *
 * ## Usage
 *
 * ```kotlin
 * override fun onReceive(context: Context, intent: Intent) {
 *     goAsync(
 *         scope = coroutineScopeProvider.default,
 *         timeout = 8.seconds,
 *         onTimeout = { Timber.w("Sync timed out") },
 *     ) {
 *         syncTopic()
 *     }
 * }
 * ```
 */
fun BroadcastReceiver.goAsync(
    scope: CoroutineScope,
    timeout: Duration? = null,
    onTimeout: () -> Unit = {},
    block: suspend CoroutineScope.() -> Unit,
): Job {
    val pendingResult: BroadcastReceiver.PendingResult? = goAsync()
    return scope.launch {
        if (timeout == null) {
            block()
        } else {
            withTimeoutOrNull(timeout) { block() } ?: onTimeout()
        }
    }.apply {
        // Also runs when the job is cancelled before it starts, which a finally block inside it would miss.
        invokeOnCompletion { pendingResult?.finish() }
    }
}
