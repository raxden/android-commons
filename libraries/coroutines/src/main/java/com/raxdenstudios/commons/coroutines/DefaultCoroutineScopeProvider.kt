package com.raxdenstudios.commons.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * Production [CoroutineScopeProvider] that owns one app-lifetime scope per dispatcher.
 *
 * [main], [io] and [default] are created once, when the provider is constructed. Each is a
 * `CoroutineScope` built from the matching dispatcher of [dispatcherProvider] plus its own
 * [SupervisorJob], so a failing child coroutine does not cancel its siblings or the scope.
 *
 * The scopes are app-lifetime: nothing ever cancels them, so they live as long as the provider does
 * (register the provider as a DI singleton). Work launched in them outlives any screen, ViewModel or
 * lifecycle, so do not use them for UI-bound work (use `viewModelScope` or `lifecycleScope` instead).
 * Callers own the jobs they launch and must cancel those jobs, never the shared scopes: a cancelled
 * scope cancels every later launch immediately, silently.
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
