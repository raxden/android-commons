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
}
