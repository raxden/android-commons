package com.raxdenstudios.commons.android.compose

import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ComposableLifecycleTest {

    private val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
    private val owner = object : LifecycleOwner {
        override val lifecycle = LifecycleRegistry.createUnsafe(this)
    }
    private val lifecycle get() = owner.lifecycle as LifecycleRegistry

    @Test
    fun `Given an observer, When the owner changes state, Then the observer receives the events`() {
        val events = mutableListOf<Lifecycle.Event>()
        activity.setContent { ComposableLifecycle({ _, event -> events += event }, owner) }
        idle()

        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertThat(events).containsExactly(Lifecycle.Event.ON_CREATE, Lifecycle.Event.ON_START).inOrder()
    }

    @Test
    fun `Given a recomposition with a new observer, When later events come, Then only the new one gets them`() {
        val oldEvents = mutableListOf<Lifecycle.Event>()
        val newEvents = mutableListOf<Lifecycle.Event>()
        var observer by mutableStateOf(LifecycleEventObserver { _, event -> oldEvents += event })
        activity.setContent { ComposableLifecycle(observer, owner) }
        idle()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        observer = LifecycleEventObserver { _, event -> newEvents += event }
        idle()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_START)

        assertThat(oldEvents).containsExactly(Lifecycle.Event.ON_CREATE)
        assertThat(newEvents).containsExactly(Lifecycle.Event.ON_START)
    }

    @Test
    fun `Given a recomposition with a new observer, When recomposed, Then it is not removed or re-added`() {
        val events = mutableListOf<Lifecycle.Event>()
        var observer by mutableStateOf(LifecycleEventObserver { _, _ -> })
        activity.setContent { ComposableLifecycle(observer, owner) }
        idle()
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)

        observer = LifecycleEventObserver { _, event -> events += event }
        idle()

        // Re-registering would replay ON_CREATE to the new registration.
        assertThat(events).isEmpty()
        assertThat(lifecycle.observerCount).isEqualTo(1)
    }

    @Test
    fun `Given a composed observer, When the composable leaves, Then it is removed from the owner`() {
        var visible by mutableStateOf(true)
        activity.setContent { if (visible) ComposableLifecycle({ _, _ -> }, owner) }
        idle()
        assertThat(lifecycle.observerCount).isEqualTo(1)

        visible = false
        idle()

        assertThat(lifecycle.observerCount).isEqualTo(0)
    }

    private fun idle() = shadowOf(Looper.getMainLooper()).idle()
}
