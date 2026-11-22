@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.os.Looper
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class DialogFragmentExtensionTest {

    private val activity = Robolectric.buildActivity(FragmentActivity::class.java).setup().get()
    private val fragmentManager = activity.supportFragmentManager
    private val owner = object : LifecycleOwner {
        override val lifecycle = LifecycleRegistry.createUnsafe(this)
    }
    private val registry get() = owner.lifecycle as LifecycleRegistry
    private val results = mutableListOf<Bundle>()

    @Before
    fun setUp() {
        registry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        registry.handleLifecycleEvent(Lifecycle.Event.ON_START)
    }

    @Test
    fun `Given a free tag, When showForResult is called, Then it shows and registers on the given owner`() {
        DialogFragment().showForResult(fragmentManager, owner, TAG) { results += it }
        shadowOf(Looper.getMainLooper()).idle()

        assertThat(fragmentManager.findFragmentByTag(TAG)).isNotNull()
        assertThat(registry.observerCount).isEqualTo(1)
    }

    @Test
    fun `Given a shown dialog, When a result with the matching key is set, Then it is delivered`() {
        DialogFragment().showForResult(fragmentManager, owner, TAG, requestKey = KEY) { results += it }
        shadowOf(Looper.getMainLooper()).idle()

        fragmentManager.setFragmentResult(KEY, bundleOf("value" to 1))

        assertThat(results).hasSize(1)
        assertThat(results.single().getInt("value")).isEqualTo(1)
    }

    @Test
    fun `Given a shown dialog, When a result with another key is set, Then it is not delivered`() {
        DialogFragment().showForResult(fragmentManager, owner, TAG, requestKey = KEY) { results += it }
        shadowOf(Looper.getMainLooper()).idle()

        fragmentManager.setFragmentResult("other", bundleOf("value" to 1))

        assertThat(results).isEmpty()
    }

    @Test
    fun `Given the tag is already present, When showForResult is called, Then nothing is registered`() {
        val existing = DialogFragment()
        fragmentManager.beginTransaction().add(existing, TAG).commitNow()

        DialogFragment().showForResult(fragmentManager, owner, TAG) { results += it }
        shadowOf(Looper.getMainLooper()).idle()
        fragmentManager.setFragmentResult(TAG, bundleOf("value" to 1))

        assertThat(fragmentManager.findFragmentByTag(TAG)).isSameInstanceAs(existing)
        assertThat(registry.observerCount).isEqualTo(0)
        assertThat(results).isEmpty()
    }

    private companion object {
        const val TAG = "tag"
        const val KEY = "key"
    }
}
