package com.raxdenstudios.commons.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class DefaultCoroutineScopeProviderTest {

    private val mainDispatcher: TestDispatcher = StandardTestDispatcher()
    private val ioDispatcher: TestDispatcher = StandardTestDispatcher()
    private val defaultDispatcher: TestDispatcher = StandardTestDispatcher()
    private val dispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher = mainDispatcher
        override val io: CoroutineDispatcher = ioDispatcher
        override val default: CoroutineDispatcher = defaultDispatcher
    }
    private val provider = DefaultCoroutineScopeProvider(dispatcherProvider)

    @After
    fun tearDown() {
        provider.main.cancel()
        provider.io.cancel()
        provider.default.cancel()
    }

    @Test
    fun `Given provider When reading scopes repeatedly Then returns same instance`() {
        assertThat(provider.main).isSameInstanceAs(provider.main)
        assertThat(provider.io).isSameInstanceAs(provider.io)
        assertThat(provider.default).isSameInstanceAs(provider.default)
    }

    @Test
    fun `Given provider When reading scopes Then each holds the matching dispatcher`() {
        assertThat(provider.main.coroutineContext[kotlin.coroutines.ContinuationInterceptor])
            .isSameInstanceAs(mainDispatcher)
        assertThat(provider.io.coroutineContext[kotlin.coroutines.ContinuationInterceptor])
            .isSameInstanceAs(ioDispatcher)
        assertThat(provider.default.coroutineContext[kotlin.coroutines.ContinuationInterceptor])
            .isSameInstanceAs(defaultDispatcher)
    }

    @Test
    fun `Given provider When reading scopes Then each has an active job and they are distinct`() {
        val jobs = listOf(provider.main, provider.io, provider.default).map { it.coroutineContext[Job] }

        assertThat(jobs).doesNotContain(null)
        assertThat(jobs.all { it!!.isActive }).isTrue()
        assertThat(jobs.toSet()).hasSize(3)
    }

    @Test
    fun `Given provider When a child fails Then scope stays active and supervisor semantics hold`() = runTest {
        val handlerCalled = CompletableDeferred<Unit>()
        val failingDispatcherScope = DefaultCoroutineScopeProvider(
            object : DispatcherProvider {
                override val main: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
                override val io: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
                override val default: CoroutineDispatcher = StandardTestDispatcher(testScheduler)
            },
        ).io
        val handler = kotlinx.coroutines.CoroutineExceptionHandler { _, _ -> handlerCalled.complete(Unit) }

        failingDispatcherScope.launch(handler) { error("boom") }
        val sibling = failingDispatcherScope.launch { }
        testScheduler.advanceUntilIdle()

        assertThat(handlerCalled.isCompleted).isTrue()
        assertThat(failingDispatcherScope.coroutineContext[Job]!!.isActive).isTrue()
        assertThat(sibling.isCancelled).isFalse()
        failingDispatcherScope.cancel()
    }
}
