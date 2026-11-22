package com.raxdenstudios.commons.android.ext

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
internal class FragmentExtensionTest {

    private lateinit var activity: FragmentActivity
    private lateinit var host: Fragment
    private lateinit var container: View

    @Before
    fun setUp() {
        activity = Robolectric.buildActivity(FragmentActivity::class.java).setup().get()
        host = Host()
        activity.supportFragmentManager.beginTransaction()
            .add(android.R.id.content, host, "host")
            .commitNow()
        container = host.requireView().findViewById(777)
    }

    @Test
    fun `Given a fragment When inflateView Then inflates the layout`() {
        val view = host.inflateView(android.R.layout.simple_list_item_1)

        assertThat(view).isNotNull()
        assertThat(view.parent).isNull()
    }

    @Test
    fun `Given a root When inflateView Then layout params come from the root`() {
        val root = FrameLayout(activity)

        val view = host.inflateView(android.R.layout.simple_list_item_1, root)

        assertThat(view).isSameInstanceAs(root)
    }

    @Test
    fun `Given an attached fragment When close Then it is removed from the activity`() {
        host.close()
        activity.supportFragmentManager.executePendingTransactions()

        assertThat(host.isAdded).isFalse()
    }

    @Test
    fun `Given a detached fragment When close Then does nothing`() {
        val detached = Fragment()

        assertThat(detached.close()).isNull()
    }

    @Test
    fun `Given no saved state When loadFragment Then creates and commits into child manager`() {
        var created = 0

        val child = host.loadFragment<Child>(container, null) {
            created++
            Child()
        }
        host.childFragmentManager.executePendingTransactions()

        assertThat(created).isEqualTo(1)
        assertThat(host.childFragmentManager.findFragmentByTag("Child")).isSameInstanceAs(child)
    }

    @Test
    fun `Given saved state and existing child When loadFragment Then returns existing`() {
        val existing = host.loadFragment<Child>(container, null) { Child() }
        host.childFragmentManager.executePendingTransactions()
        var created = 0

        val result = host.loadFragment<Child>(container, Bundle()) {
            created++
            Child()
        }

        assertThat(created).isEqualTo(0)
        assertThat(result).isSameInstanceAs(existing)
    }

    @Test
    fun `Given saved state and no child When loadFragment Then creates a new one`() {
        val result = host.loadFragment<Child>(container, Bundle()) { Child() }

        assertThat(result).isNotNull()
    }

    @Test
    fun `Given no dialog When loadDialogFragment Then creates it and applies cancelable`() {
        val dialog = host.loadDialogFragment<ChildDialog>("tag", isCancelable = true) { ChildDialog() }

        assertThat(dialog.isCancelable).isTrue()
        assertThat(host.childFragmentManager.findFragmentByTag("tag")).isSameInstanceAs(dialog)
    }

    @Test
    fun `Given existing dialog When loadDialogFragment Then reuses it`() {
        val first = host.loadDialogFragment<ChildDialog>("tag") { ChildDialog() }
        var created = 0

        val second = host.loadDialogFragment<ChildDialog>("tag") {
            created++
            ChildDialog()
        }

        assertThat(created).isEqualTo(0)
        assertThat(second).isSameInstanceAs(first)
        assertThat(second.isCancelable).isFalse()
    }

    @Test
    fun `Given back-to-back calls When loadDialogFragment Then returns the same instance`() {
        var created = 0

        val first = host.loadDialogFragment<ChildDialog>("tag") {
            created++
            ChildDialog()
        }
        val second = host.loadDialogFragment<ChildDialog>("tag") {
            created++
            ChildDialog()
        }

        assertThat(created).isEqualTo(1)
        assertThat(second).isSameInstanceAs(first)
    }

    class Host : Fragment() {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?,
        ): View = FrameLayout(inflater.context).apply {
            addView(FrameLayout(inflater.context).apply { id = 777 })
        }
    }

    class Child : Fragment()

    class ChildDialog : DialogFragment()
}
