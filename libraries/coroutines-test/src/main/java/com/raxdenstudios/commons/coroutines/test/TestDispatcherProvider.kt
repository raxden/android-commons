package com.raxdenstudios.commons.coroutines.test

import com.raxdenstudios.commons.coroutines.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Test double for [DispatcherProvider] that returns the same test [dispatcher] for [main], [io] and
 * [default].
 *
 * Use it to replace mockk stubs of the dispatcher provider in unit tests. The default
 * [UnconfinedTestDispatcher] runs coroutines eagerly, so assertions do not need explicit
 * advancing. When no scheduler is passed, the dispatcher reuses the scheduler of an installed
 * `Dispatchers.setMain` test dispatcher, otherwise it creates its own.
 *
 * ## Usage
 *
 * ```kotlin
 * @Test
 * fun `loads items on io`() = runTest {
 *     val provider = TestDispatcherProvider(StandardTestDispatcher(testScheduler))
 *     val useCase = LoadItemsUseCase(provider, repository)
 *
 *     useCase.execute()
 *     advanceUntilIdle()
 * }
 * ```
 *
 * @property dispatcher the dispatcher returned for every dispatcher property.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestDispatcherProvider(
    val dispatcher: CoroutineDispatcher = UnconfinedTestDispatcher(),
) : DispatcherProvider {

    override val main: CoroutineDispatcher = dispatcher
    override val io: CoroutineDispatcher = dispatcher
    override val default: CoroutineDispatcher = dispatcher
}
