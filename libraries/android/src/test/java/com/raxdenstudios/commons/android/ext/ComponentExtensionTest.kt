package com.raxdenstudios.commons.android.ext

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ComponentExtensionTest {

    private val lifecycle: Lifecycle = mockk()
    private val activity: ComponentActivity = mockk {
        every { this@mockk.lifecycle } returns lifecycle
    }

    @Test
    fun `given INITIALIZED activity, when isAtLeastCreated is called then return false`() {
        every { lifecycle.currentState } returns Lifecycle.State.INITIALIZED

        assertFalse(activity.isAtLeastCreated())
    }

    @Test
    fun `given CREATED activity, when lifecycle checks are called then return expected values`() {
        every { lifecycle.currentState } returns Lifecycle.State.CREATED

        assertTrue(activity.isAtLeastCreated())
        assertFalse(activity.isAtLeastStarted())
        assertFalse(activity.isAtLeastResumed())
    }

    @Test
    fun `given STARTED activity, when lifecycle checks are called then return expected values`() {
        every { lifecycle.currentState } returns Lifecycle.State.STARTED

        assertTrue(activity.isAtLeastCreated())
        assertTrue(activity.isAtLeastStarted())
        assertFalse(activity.isAtLeastResumed())
    }

    @Test
    fun `given RESUMED activity, when lifecycle checks are called then return expected values`() {
        every { lifecycle.currentState } returns Lifecycle.State.RESUMED

        assertTrue(activity.isAtLeastCreated())
        assertTrue(activity.isAtLeastStarted())
        assertTrue(activity.isAtLeastResumed())
    }

    @Test
    fun `given DESTROYED activity, when lifecycle checks are called then return false`() {
        every { lifecycle.currentState } returns Lifecycle.State.DESTROYED

        assertFalse(activity.isAtLeastCreated())
        assertFalse(activity.isAtLeastStarted())
        assertFalse(activity.isAtLeastResumed())
    }
}
