package com.raxdenstudios.commons.coroutines.ext

import android.util.Log
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.spyk
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@DelicateCoroutinesApi
class CoroutinesExtensionTest {

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
        val job = safeLaunch {}

        assertThat(job).isInstanceOf(Job::class.java)
    }

    @Test
    fun `use safeLaunch with exception`() {
        val scope = CoroutineScope(newSingleThreadContext("t1"))

        val job = scope.safeLaunch { throw IllegalStateException() }

        assertThat(job).isInstanceOf(Job::class.java)
    }

    @Test
    fun `use launch with exception`() {
        val scope = CoroutineScope(newSingleThreadContext("t1"))
        val onError = spyk<(Throwable) -> Unit>()

        val job = scope.launch(
            onError = onError,
            block = { throw IllegalStateException() }
        )

        verify { onError.invoke(any()) }
        assertThat(job).isInstanceOf(Job::class.java)
    }
}
