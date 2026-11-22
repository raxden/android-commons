package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class FragmentActivityExtensionTest {

    private lateinit var activity: FragmentActivity
    private lateinit var container: View

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(FragmentActivity::class.java).setup().get()
        container = FrameLayout(activity).apply { id = 12345 }
        activity.setContentView(container)
    }

    @Test
    fun `Given no saved state When loadFragment Then creates and commits the fragment`() {
        var created = 0

        val fragment = activity.loadFragment<TestFragment>(container, null) {
            created++
            TestFragment()
        }
        activity.supportFragmentManager.executePendingTransactions()

        assertThat(created).isEqualTo(1)
        assertThat(fragment.isAdded).isTrue()
        assertThat(activity.supportFragmentManager.findFragmentByTag("TestFragment"))
            .isSameInstanceAs(fragment)
    }

    @Test
    fun `Given saved state and fragment in container When loadFragment Then returns existing`() {
        val existing = activity.loadFragment<TestFragment>(container, null) { TestFragment() }
        activity.supportFragmentManager.executePendingTransactions()
        var created = 0

        val result = activity.loadFragment<TestFragment>(container, Bundle()) {
            created++
            TestFragment()
        }

        assertThat(created).isEqualTo(0)
        assertThat(result).isSameInstanceAs(existing)
    }

    @Test
    fun `Given saved state and no fragment When loadFragment Then creates a new one`() {
        var created = 0

        val result = activity.loadFragment<TestFragment>(container, Bundle()) {
            created++
            TestFragment()
        }
        activity.supportFragmentManager.executePendingTransactions()

        assertThat(created).isEqualTo(1)
        assertThat(result.isAdded).isTrue()
        assertThat(activity.supportFragmentManager.findFragmentByTag("TestFragment"))
            .isSameInstanceAs(result)
    }

    @Test
    fun `Given an added fragment When closeFragment Then it is removed`() {
        val fragment = activity.loadFragment<TestFragment>(container, null) { TestFragment() }
        activity.supportFragmentManager.executePendingTransactions()

        activity.closeFragment(fragment)
        activity.supportFragmentManager.executePendingTransactions()

        assertThat(fragment.isAdded).isFalse()
    }

    @Test
    fun `Given no dialog with tag When loadDialogFragment Then creates, adds and sets cancelable`() {
        val dialog = activity.loadDialogFragment<TestDialog>("tag", isCancelable = true) { TestDialog() }

        assertThat(dialog.isCancelable).isTrue()
        assertThat(activity.supportFragmentManager.findFragmentByTag("tag")).isSameInstanceAs(dialog)
    }

    @Test
    fun `Given default cancelable When loadDialogFragment Then it is not cancelable`() {
        val dialog = activity.loadDialogFragment<TestDialog>("tag") { TestDialog() }

        assertThat(dialog.isCancelable).isFalse()
    }

    @Test
    fun `Given dialog already added When loadDialogFragment Then returns existing without creating`() {
        val first = activity.loadDialogFragment<TestDialog>("tag") { TestDialog() }
        var created = 0

        val second = activity.loadDialogFragment<TestDialog>("tag", isCancelable = true) {
            created++
            TestDialog()
        }

        assertThat(created).isEqualTo(0)
        assertThat(second).isSameInstanceAs(first)
        assertThat(second.isCancelable).isTrue()
    }

    @Test
    fun `Given back-to-back calls When loadDialogFragment Then returns the same instance`() {
        var created = 0

        val first = activity.loadDialogFragment<TestDialog>("tag") {
            created++
            TestDialog()
        }
        val second = activity.loadDialogFragment<TestDialog>("tag") {
            created++
            TestDialog()
        }

        assertThat(created).isEqualTo(1)
        assertThat(second).isSameInstanceAs(first)
    }

    class TestFragment : Fragment()

    class TestDialog : DialogFragment()
}
