package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.app.Application
import android.os.Bundle
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ApplicationExtensionTest {

    private val application: Application = RuntimeEnvironment.getApplication()

    @Test
    fun `Given an application When initCompatVector Then does not throw`() {
        application.initCompatVector()
    }

    @Test
    fun `Given an action When doOnActivityCreated Then returns callbacks that run the action`() {
        val created = mutableListOf<Activity>()
        val callbacks = application.doOnActivityCreated<TestActivity> { created += it }
        val activity = Robolectric.buildActivity(TestActivity::class.java).get()

        callbacks.onActivityCreated(activity, null)

        assertEquals(listOf<Activity>(activity), created)
        application.unregisterActivityLifecycleCallbacks(callbacks)
    }

    @Test
    fun `Given matching activity When created Then runs action`() {
        val created = mutableListOf<Activity>()
        val callbacks = application.doOnActivityCreated<TestActivity> { created += it }

        val controller = Robolectric.buildActivity(TestActivity::class.java).create()

        assertEquals(listOf<Activity>(controller.get()), created)
        application.unregisterActivityLifecycleCallbacks(callbacks)
    }

    @Test
    fun `Given subclass of A When created Then runs action`() {
        val created = mutableListOf<Activity>()
        val callbacks = application.doOnActivityCreated<TestActivity> { created += it }

        val controller = Robolectric.buildActivity(SubTestActivity::class.java).create()

        assertEquals(listOf<Activity>(controller.get()), created)
        application.unregisterActivityLifecycleCallbacks(callbacks)
    }

    @Test
    fun `Given unrelated activity When created Then does not run action`() {
        val created = mutableListOf<Activity>()
        val callbacks = application.doOnActivityCreated<TestActivity> { created += it }

        Robolectric.buildActivity(OtherActivity::class.java).create()

        assertEquals(emptyList<Activity>(), created)
        application.unregisterActivityLifecycleCallbacks(callbacks)
    }

    @Test
    fun `Given other lifecycle callbacks When dispatched Then does not run action`() {
        val created = mutableListOf<Activity>()
        val callbacks = application.doOnActivityCreated<TestActivity> { created += it }
        val activity = Robolectric.buildActivity(TestActivity::class.java).get()

        callbacks.onActivityStarted(activity)
        callbacks.onActivityResumed(activity)
        callbacks.onActivityPaused(activity)
        callbacks.onActivityStopped(activity)
        callbacks.onActivitySaveInstanceState(activity, Bundle())
        callbacks.onActivityDestroyed(activity)

        assertEquals(emptyList<Activity>(), created)
        application.unregisterActivityLifecycleCallbacks(callbacks)
    }

    @Test
    fun `Given unregistered callbacks When activity created Then does not run action`() {
        val created = mutableListOf<Activity>()
        val callbacks = application.doOnActivityCreated<TestActivity> { created += it }
        application.unregisterActivityLifecycleCallbacks(callbacks)

        Robolectric.buildActivity(TestActivity::class.java).create()

        assertEquals(emptyList<Activity>(), created)
    }

    private open class TestActivity : Activity()

    private class SubTestActivity : TestActivity()

    private class OtherActivity : Activity()
}
