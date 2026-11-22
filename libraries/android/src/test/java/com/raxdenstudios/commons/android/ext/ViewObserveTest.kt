package com.raxdenstudios.commons.android.ext

import android.view.View
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.setViewTreeLifecycleOwner
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ViewObserveTest {

    private val liveData: LiveData<String> = mockk(relaxed = true)

    @Test
    fun `Given a view tree owner and an owner context, When observe is called, Then the view tree owner is used`() {
        val activity = Robolectric.buildActivity(ComponentActivity::class.java).get()
        val treeOwner: LifecycleOwner = mockk(relaxed = true)
        val view = View(activity)
        view.setViewTreeLifecycleOwner(treeOwner)

        view.observe(liveData) {}

        verify { liveData.observe(treeOwner, any()) }
    }

    @Test
    fun `Given no view tree owner, When observe is called, Then the context owner is used`() {
        val activity = Robolectric.buildActivity(ComponentActivity::class.java).get()

        View(activity).observe(liveData) {}

        verify { liveData.observe(activity, any()) }
    }

    @Test
    fun `Given no tree owner and no owner context, When observe is called, Then throw IllegalStateException`() {
        val view = View(RuntimeEnvironment.getApplication())

        assertThrows(IllegalStateException::class.java) { view.observe(liveData) {} }
    }
}
