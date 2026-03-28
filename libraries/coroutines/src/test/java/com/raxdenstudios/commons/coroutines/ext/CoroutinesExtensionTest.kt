package com.raxdenstudios.commons.coroutines.ext

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
internal class CoroutinesExtensionTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val scope = CoroutineScope(mainDispatcherRule.testDispatcher)

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun after() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `use safeLaunch`() = runTest {
        val job = scope.safeLaunch {}

        assertThat(job).isNotNull()
        assertThat(job).isInstanceOf(Job::class.java)
    }

    @Test
    fun `use safeLaunch with exception`() = runTest {
        val job = scope.safeLaunch { error("IllegalStateException") }

        assertThat(job).isNotNull()
        assertThat(job).isInstanceOf(Job::class.java)
    }

    @Test
    fun `use launch with onError callback`() = runTest {
        var errorCaught = false
        var errorMessage: String? = null

        val job = scope.launch(
            onError = { throwable ->
                errorCaught = true
                errorMessage = throwable.message
            }
        ) {
            error("Test error")
        }

        job.join()

        assertThat(job).isNotNull()
        assertThat(job).isInstanceOf(Job::class.java)
        assertThat(errorCaught).isTrue()
        assertThat(errorMessage).isEqualTo("Test error")
    }

    @Test
    fun `use launch without exception`() = runTest {
        var errorCaught = false
        var executionCompleted = false

        val job = scope.launch(
            onError = { errorCaught = true }
        ) {
            executionCompleted = true
        }

        job.join()

        assertThat(job).isNotNull()
        assertThat(job).isInstanceOf(Job::class.java)
        assertThat(errorCaught).isFalse()
        assertThat(executionCompleted).isTrue()
    }

    @Test
    fun `use safeLaunch with custom exception handler`() = runTest {
        var customHandlerCalled = false
        val customHandler = kotlinx.coroutines.CoroutineExceptionHandler { _, _ ->
            customHandlerCalled = true
        }

        val job = scope.safeLaunch(
            exceptionHandler = customHandler
        ) {
            error("Custom handler test")
        }

        job.join()

        assertThat(job).isNotNull()
        assertThat(job).isInstanceOf(Job::class.java)
        assertThat(customHandlerCalled).isTrue()
    }
}
