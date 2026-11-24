package com.raxdenstudios.commons.android.ext

import android.content.BroadcastReceiver
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
internal class BroadcastReceiverExtensionTest {

    private val pendingResult: BroadcastReceiver.PendingResult = mockk(relaxed = true)
    private val receiver: BroadcastReceiver = mockk()
    private val testScope = TestScope(StandardTestDispatcher())

    private fun stubPendingResult(
        result: BroadcastReceiver.PendingResult? = pendingResult,
    ) {
        every { receiver.goAsync() } returns result
    }

    @Test
    fun `Given a block When goAsync is called Then block runs and result is finished`() = testScope.runTest {
        stubPendingResult()
        var executed = false

        receiver.goAsync(scope = this) { executed = true }
        advanceUntilIdle()

        assertThat(executed).isTrue()
        verify(exactly = 1) { pendingResult.finish() }
    }

    @Test
    fun `Given a block When goAsync is called Then it returns a completed job`() = testScope.runTest {
        stubPendingResult()

        val job = receiver.goAsync(scope = this) { }
        assertThat(job.isActive).isTrue()
        advanceUntilIdle()

        assertThat(job.isCompleted).isTrue()
    }

    @Test
    fun `Given a block that throws When goAsync is called Then result is finished`() {
        stubPendingResult()
        val scope = TestScope(StandardTestDispatcher())
        val handler = CoroutineExceptionHandler { _, _ -> }
        val safeScope = CoroutineScope(scope.coroutineContext + handler)
        val job = receiver.goAsync(scope = safeScope) { error("boom") }

        scope.advanceUntilIdle()

        assertThat(job.isCancelled).isTrue()
        verify(exactly = 1) { pendingResult.finish() }
    }

    @Test
    fun `Given a block that exceeds timeout When goAsync is called Then onTimeout and finish are called`() =
        testScope.runTest {
            stubPendingResult()
            var timedOut = false

            receiver.goAsync(scope = this, timeout = 5.seconds, onTimeout = { timedOut = true }) {
                delay(10.seconds)
            }
            advanceTimeBy(5.seconds)
            runCurrent()

            assertThat(timedOut).isTrue()
            verify(exactly = 1) { pendingResult.finish() }
        }

    @Test
    fun `Given a block finishing before timeout When goAsync is called Then onTimeout is not called`() =
        testScope.runTest {
            stubPendingResult()
            var timedOut = false

            receiver.goAsync(scope = this, timeout = 5.seconds, onTimeout = { timedOut = true }) {
                delay(1.seconds)
            }
            advanceUntilIdle()

            assertThat(timedOut).isFalse()
            verify(exactly = 1) { pendingResult.finish() }
        }

    @Test
    fun `Given no timeout When goAsync is called Then onTimeout is not called`() = testScope.runTest {
        stubPendingResult()
        var timedOut = false

        receiver.goAsync(scope = this, onTimeout = { timedOut = true }) { delay(100.seconds) }
        advanceUntilIdle()

        assertThat(timedOut).isFalse()
    }

    @Test
    fun `Given a running block When job is cancelled Then result is finished`() = testScope.runTest {
        stubPendingResult()
        val started = CompletableDeferred<Unit>()

        val job = receiver.goAsync(scope = this) {
            started.complete(Unit)
            delay(100.seconds)
        }
        runCurrent()
        assertThat(started.isCompleted).isTrue()
        job.cancel()
        runCurrent()

        assertThat(job.isCancelled).isTrue()
        verify(exactly = 1) { pendingResult.finish() }
    }

    @Test
    fun `Given a running block When scope is cancelled Then result is finished`() {
        stubPendingResult()
        val scope = TestScope(StandardTestDispatcher())

        receiver.goAsync(scope = scope) { delay(100.seconds) }
        scope.runCurrent()
        scope.cancel()
        scope.runCurrent()

        verify(exactly = 1) { pendingResult.finish() }
    }

    @Test
    fun `Given a scope cancelled before start When goAsync is called Then block is skipped and finish called once`() {
        stubPendingResult()
        val scope = TestScope(StandardTestDispatcher())
        scope.cancel()
        var executed = false

        val job = receiver.goAsync(scope = scope) { executed = true }
        scope.runCurrent()

        assertThat(executed).isFalse()
        assertThat(job.isCancelled).isTrue()
        verify(exactly = 1) { pendingResult.finish() }
    }

    @Test
    fun `Given goAsync returns null When goAsync is called Then block runs without crashing`() = testScope.runTest {
        stubPendingResult(null)
        var executed = false

        receiver.goAsync(scope = this) { executed = true }
        advanceUntilIdle()

        assertThat(executed).isTrue()
    }

    @Test
    fun `Given goAsync returns null When block throws Then only the block exception surfaces`() {
        stubPendingResult(null)
        val scope = TestScope(StandardTestDispatcher())
        var caught: Throwable? = null
        val handler = CoroutineExceptionHandler { _, _ -> }
        val safeScope = CoroutineScope(scope.coroutineContext + handler)

        val job = receiver.goAsync(scope = safeScope) { error("boom") }
        job.invokeOnCompletion { caught = it }
        scope.advanceUntilIdle()

        assertThat(job.isCancelled).isTrue()
        assertThat(caught).isInstanceOf(IllegalStateException::class.java)
        assertThat(caught).hasMessageThat().isEqualTo("boom")
    }
}
