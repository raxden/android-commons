package com.raxdenstudios.commons.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Production [CoroutineScopeProvider] that owns one app-lifetime scope per dispatcher.
 *
 * [main], [io] and [default] are created once, when the provider is constructed. Each is a
 * `CoroutineScope` built from the matching dispatcher of [dispatcherProvider] plus its own
 * [SupervisorJob], so a failing child coroutine does not cancel its siblings or the scope. The
 * provider never cancels these scopes; they live as long as the provider does. Callers should cancel
 * the jobs they launch rather than the shared scopes.
 *
 * ## Usage
 *
 * ```kotlin
 * val scopeProvider: CoroutineScopeProvider = DefaultCoroutineScopeProvider()
 *
 * scopeProvider.io.launch { /* blocking work */ }
 * ```
 *
 * @param dispatcherProvider source of the dispatchers each scope runs on. Defaults to
 * [DefaultDispatcherProvider].
 */
class DefaultCoroutineScopeProvider(
    dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider(),
) : CoroutineScopeProvider {

    override val main: CoroutineScope = CoroutineScope(dispatcherProvider.main + SupervisorJob())
    override val io: CoroutineScope = CoroutineScope(dispatcherProvider.io + SupervisorJob())
    override val default: CoroutineScope = CoroutineScope(dispatcherProvider.default + SupervisorJob())
}
