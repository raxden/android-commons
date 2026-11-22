package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ViewExtensionTest {

    private val context: Context = RuntimeEnvironment.getApplication()
    private lateinit var view: View

    @Before
    fun setUp() {
        view = View(context)
    }

    @After
    fun tearDown() {
        unmockkStatic(SystemClock::class)
    }

    @Test
    fun `Given a view When setPaddingTop Then only top padding changes`() {
        view.setPaddingRelative(1, 2, 3, 4)

        view.setPaddingTop(10)

        assertThat(view.paddingStart).isEqualTo(1)
        assertThat(view.paddingTop).isEqualTo(10)
        assertThat(view.paddingEnd).isEqualTo(3)
        assertThat(view.paddingBottom).isEqualTo(4)
    }

    @Test
    fun `Given a view When toggling selectable Then isSelected follows`() {
        view.doItSelectable()
        assertThat(view.isSelected).isTrue()

        view.doItUnselectable()
        assertThat(view.isSelected).isFalse()
    }

    @Test
    fun `Given a view When toggling clickable Then isClickable follows`() {
        view.doItClickable()
        assertThat(view.isClickable).isTrue()

        view.doItNonClickable()
        assertThat(view.isClickable).isFalse()
    }

    @Test
    fun `Given visibleGone When true Then VISIBLE and when false Then GONE`() {
        view.visibleGone(true)
        assertThat(view.visibility).isEqualTo(View.VISIBLE)
        assertThat(view.isVisible()).isTrue()
        assertThat(view.isGone()).isFalse()

        view.visibleGone(false)
        assertThat(view.visibility).isEqualTo(View.GONE)
        assertThat(view.isGone()).isTrue()
        assertThat(view.isVisible()).isFalse()
    }

    @Test
    fun `Given an invisible view When isInvisible Then returns true`() {
        view.visibility = View.INVISIBLE

        assertThat(view.isInvisible()).isTrue()

        view.visibility = View.VISIBLE
        assertThat(view.isInvisible()).isFalse()
    }

    @Test
    fun `Given a color resource When getColor Then returns resolved color`() {
        assertThat(view.getColor(android.R.color.white)).isEqualTo(0xFFFFFFFF.toInt())
    }

    @Test
    fun `Given a drawable resource When getDrawable Then returns a drawable`() {
        assertThat(view.getDrawable(android.R.drawable.ic_menu_add)).isNotNull()
    }

    @Test
    fun `Given a view group When inflateView Then returns the view not attached`() {
        val parent = FrameLayout(context)

        val result = parent.inflateView(android.R.layout.simple_list_item_1)

        assertThat(result).isNotNull()
        assertThat(result.parent).isNull()
        assertThat(parent.childCount).isEqualTo(0)
    }

    @Test
    fun `Given attachToRoot true When inflateView Then returns the parent with child attached`() {
        val parent = FrameLayout(context)

        val result = parent.inflateView(android.R.layout.simple_list_item_1, attachToRoot = true)

        assertThat(result).isSameInstanceAs(parent)
        assertThat(parent.childCount).isEqualTo(1)
    }

    @Test
    fun `Given not in edit mode When inflateViewIfEditMode Then nothing is inflated`() {
        val parent = FrameLayout(context)

        parent.inflateViewIfEditMode(android.R.layout.simple_list_item_1, attachToRoot = true)

        assertThat(parent.childCount).isEqualTo(0)
    }

    @Test
    fun `Given in edit mode When inflateViewIfEditMode Then layout is inflated`() {
        val parent = object : FrameLayout(context) {
            override fun isInEditMode(): Boolean = true
        }

        parent.inflateViewIfEditMode(android.R.layout.simple_list_item_1, attachToRoot = true)

        assertThat(parent.childCount).isEqualTo(1)
    }

    @Test
    fun `Given a view When starting fade animations Then an animation is set`() {
        view.startFadeInAnimation()
        assertThat(view.animation).isNotNull()

        view.clearAnimation()
        view.startFadeOutAnimation()
        assertThat(view.animation).isNotNull()
    }

    @Test
    fun `Given quick consecutive clicks When safe click listener Then second is debounced`() {
        mockkStatic(SystemClock::class)
        every { SystemClock.elapsedRealtime() } returnsMany listOf(1000L, 1000L, 1100L, 1700L, 1700L)
        var clicks = 0
        view.setSafeOnClickListener(debounceTime = 500) { clicks++ }

        view.performClick()
        view.performClick()
        view.performClick()

        assertThat(clicks).isEqualTo(2)
    }

    @Test
    fun `Given attrs When useStyledAttributes Then block runs with typed array`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()
        val styleable = intArrayOf(android.R.attr.text)
        var invoked = false

        View(activity).useStyledAttributes(null as AttributeSet?, styleable) { invoked = true }

        assertThat(invoked).isTrue()
    }

    @Test
    fun `Given a linear layout When inflateView Then uses the layout context inflater`() {
        val parent = LinearLayout(context)

        assertThat(parent.inflateView(android.R.layout.simple_list_item_1)).isNotNull()
    }
}
