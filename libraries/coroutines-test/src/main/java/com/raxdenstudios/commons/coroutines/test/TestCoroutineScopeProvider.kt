package com.raxdenstudios.commons.coroutines.test

import com.raxdenstudios.commons.coroutines.CoroutineScopeProvider
import kotlinx.coroutines.CoroutineScope

/**
 * Test double for [CoroutineScopeProvider] that returns the same [scope] for [main], [io] and
 * [default].
 *
 * Use it to replace mockk stubs of the scope provider in unit tests. Passing the scope of a test
 * keeps every launched coroutine on the test scheduler, so the test controls its timing.
 *
 * ## Usage
 *
 * ```kotlin
 * @Test
 * fun `loads items on io`() = runTest {
 *     val provider = TestCoroutineScopeProvider(backgroundScope)
 *     val viewModel = ItemsViewModel(provider, repository)
 *
 *     viewModel.load()
 *     advanceUntilIdle()
 * }
 * ```
 *
 * @property scope the scope returned for every dispatcher. `backgroundScope` from `runTest` is
 * cancelled automatically when the test ends.
 */
class TestCoroutineScopeProvider(
    val scope: CoroutineScope,
) : CoroutineScopeProvider {

    override val main: CoroutineScope = scope
    override val io: CoroutineScope = scope
    override val default: CoroutineScope = scope
}
