package com.raxdenstudios.commons.android.util

import android.app.Activity
import android.os.Bundle
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Test

internal class ActivityLifecycleCallbacksAdapterTest {

    private val activity: Activity = mockk(relaxed = true)
    private val bundle: Bundle = mockk(relaxed = true)

    @Test
    fun `Given plain adapter When any callback is invoked Then does nothing and does not throw`() {
        val adapter = ActivityLifecycleCallbacksAdapter()

        adapter.onActivityCreated(activity, bundle)
        adapter.onActivityCreated(activity, null)
        adapter.onActivityStarted(activity)
        adapter.onActivityResumed(activity)
        adapter.onActivityPaused(activity)
        adapter.onActivityStopped(activity)
        adapter.onActivitySaveInstanceState(activity, bundle)
        adapter.onActivityDestroyed(activity)

        verify(exactly = 0) { activity.finish() }
    }

    @Test
    fun `Given subclass overriding one callback When all callbacks are invoked Then only that one is received`() {
        val received = mutableListOf<String>()
        val adapter = object : ActivityLifecycleCallbacksAdapter() {
            override fun onActivityResumed(
                activity: Activity,
            ) {
                received += "resumed"
            }
        }

        adapter.onActivityCreated(activity, bundle)
        adapter.onActivityStarted(activity)
        adapter.onActivityResumed(activity)
        adapter.onActivityPaused(activity)
        adapter.onActivityStopped(activity)
        adapter.onActivitySaveInstanceState(activity, bundle)
        adapter.onActivityDestroyed(activity)

        assertEquals(listOf("resumed"), received)
    }
}
