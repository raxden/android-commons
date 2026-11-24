package com.raxdenstudios.commons.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class MutexSuspendLockTest {

    private val lock: SuspendLock = MutexSuspendLock()

    @Test
    fun `Given a block When withLock is called Then returns the block value`() = runTest {
        val result = lock.withLock { "value" }

        assertThat(result).isEqualTo("value")
    }

    @Test
    fun `Given a holder When a second caller arrives Then it waits for the first`() = runTest {
        val events = mutableListOf<String>()
        val gate = CompletableDeferred<Unit>()

        launch {
            lock.withLock {
                events.add("first-start")
                gate.await()
                events.add("first-end")
            }
        }
        runCurrent()
        launch {
            lock.withLock { events.add("second") }
        }
        runCurrent()

        assertThat(events).containsExactly("first-start").inOrder()

        gate.complete(Unit)
        runCurrent()

        assertThat(events).containsExactly("first-start", "first-end", "second").inOrder()
    }

    @Test
    fun `Given a throwing block When withLock is called Then it propagates and releases`() = runTest {
        var failure: IllegalStateException? = null
        try {
            lock.withLock { throw IllegalStateException("boom") }
        } catch (expected: IllegalStateException) {
            failure = expected
        }

        assertThat(failure).hasMessageThat().isEqualTo("boom")

        val result = lock.withLock { "after" }

        assertThat(result).isEqualTo("after")
    }

    @Test
    fun `Given a cancelled holder When another caller arrives Then lock is released`() = runTest {
        val gate = CompletableDeferred<Unit>()
        val holder = launch { lock.withLock { gate.await() } }
        runCurrent()
        var acquired = false
        launch { lock.withLock { acquired = true } }
        runCurrent()
        assertThat(acquired).isFalse()

        holder.cancel()
        runCurrent()

        assertThat(acquired).isTrue()
    }

    @Test
    fun `Given two separate instances When one is held Then the other is not blocked`() = runTest {
        val otherLock: SuspendLock = MutexSuspendLock()
        val gate = CompletableDeferred<Unit>()
        launch { lock.withLock { gate.await() } }
        runCurrent()

        val result = otherLock.withLock { "free" }

        assertThat(result).isEqualTo("free")
        // Release the holder so runTest can finish; not part of the assertions.
        gate.complete(Unit)
    }
}
