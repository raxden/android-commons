package com.raxdenstudios.commons.android

import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class ActivityHolderExtensionTest {

    private val lifecycle: Lifecycle = mockk()
    private val activity: ComponentActivity = mockk()
    private val holder = ActivityHolder()

    init {
        every { activity.lifecycle } returns lifecycle
    }

    @Test
    fun `Given no attached activity, When resumedActivity is called, Then return null`() {
        assertThat(holder.resumedActivity()).isNull()
    }

    @Test
    fun `Given a resumed activity, When resumedActivity is called, Then return it`() {
        every { lifecycle.currentState } returns Lifecycle.State.RESUMED
        holder.attach(activity)

        assertThat(holder.resumedActivity()).isSameInstanceAs(activity)
    }

    @Test
    fun `Given a started but not resumed activity, When resumedActivity is called, Then return null`() {
        every { lifecycle.currentState } returns Lifecycle.State.STARTED
        holder.attach(activity)

        assertThat(holder.resumedActivity()).isNull()
    }

    @Test
    fun `Given a destroyed activity, When resumedActivity is called, Then return null`() {
        every { lifecycle.currentState } returns Lifecycle.State.DESTROYED
        holder.attach(activity)

        assertThat(holder.resumedActivity()).isNull()
    }
}
