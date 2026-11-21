package com.raxdenstudios.commons.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * Production [DispatcherProvider] backed by the standard [Dispatchers.Main], [Dispatchers.IO] and
 * [Dispatchers.Default].
 *
 * Use it as the single source of dispatchers in DI instead of hand-rolling an implementation. The
 * properties are resolved once, when the provider is created. [main] needs a main dispatcher at
 * dispatch time (on Android this is always available); on plain JVM it fails only when used, unless
 * a main dispatcher is installed.
 *
 * ## Usage
 *
 * ```kotlin
 * val dispatcherProvider: DispatcherProvider = DefaultDispatcherProvider()
 *
 * scope.launch(dispatcherProvider.io) { /* blocking work */ }
 * ```
 */
class DefaultDispatcherProvider : DispatcherProvider {

    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}
