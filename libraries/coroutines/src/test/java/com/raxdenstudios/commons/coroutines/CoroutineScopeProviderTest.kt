package com.raxdenstudios.commons.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
internal class CoroutineScopeProviderTest {

    @Test
    fun `verify default implementation provides correct scopes`() {
        val scopeProvider = object : CoroutineScopeProvider {
            override val main: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
            override val io: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
            override val default: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
        }

        assertThat(scopeProvider.main).isNotNull()
        assertThat(scopeProvider.io).isNotNull()
        assertThat(scopeProvider.default).isNotNull()

        scopeProvider.main.cancel()
        scopeProvider.io.cancel()
        scopeProvider.default.cancel()
    }

    @Test
    fun `verify test implementation can use test scopes`() = runTest {
        val testDispatcher = kotlinx.coroutines.test.UnconfinedTestDispatcher()

        val scopeProvider = object : CoroutineScopeProvider {
            override val main: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
            override val io: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
            override val default: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
        }

        assertThat(scopeProvider.main).isNotNull()
        assertThat(scopeProvider.io).isNotNull()
        assertThat(scopeProvider.default).isNotNull()

        scopeProvider.main.cancel()
        scopeProvider.io.cancel()
        scopeProvider.default.cancel()
    }

    @Test
    fun `verify scopes can be used for launching coroutines`() = runTest {
        val testDispatcher = kotlinx.coroutines.test.UnconfinedTestDispatcher()
        var executionCount = 0

        val scopeProvider = object : CoroutineScopeProvider {
            override val main: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
            override val io: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
            override val default: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
        }

        scopeProvider.main.launch {
            executionCount++
        }

        scopeProvider.io.launch {
            executionCount++
        }

        scopeProvider.default.launch {
            executionCount++
        }

        testScheduler.advanceUntilIdle()

        assertThat(executionCount).isEqualTo(3)

        scopeProvider.main.cancel()
        scopeProvider.io.cancel()
        scopeProvider.default.cancel()
    }

    @Test
    fun `verify scope cancellation stops coroutines`() = runTest {
        val testDispatcher = kotlinx.coroutines.test.StandardTestDispatcher(testScheduler)
        var executionCompleted = false

        val scopeProvider = object : CoroutineScopeProvider {
            override val main: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
            override val io: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
            override val default: CoroutineScope = CoroutineScope(testDispatcher + SupervisorJob())
        }

        scopeProvider.main.launch {
            delay(1000)
            executionCompleted = true
        }

        scopeProvider.main.cancel()
        testScheduler.advanceUntilIdle()

        assertThat(executionCompleted).isFalse()

        scopeProvider.io.cancel()
        scopeProvider.default.cancel()
    }
}
