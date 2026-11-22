package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.google.common.truth.Truth.assertThat
import kotlinx.parcelize.Parcelize
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ActivityExtensionTest {

    @Test
    fun `Given an activity When setFullScreen Then decor view hides navigation and goes fullscreen`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()

        activity.setFullScreen()

        @Suppress("DEPRECATION")
        val flags = activity.window.decorView.systemUiVisibility
        val expected = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        assertThat(flags and expected).isEqualTo(expected)
    }

    @Test
    fun `Given a previously set flag When setFullScreen Then the flag is kept`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()
        @Suppress("DEPRECATION")
        activity.window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        activity.setFullScreen()

        @Suppress("DEPRECATION")
        val flags = activity.window.decorView.systemUiVisibility
        val expected = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
            View.SYSTEM_UI_FLAG_FULLSCREEN
        assertThat(flags and expected).isEqualTo(expected)
    }

    @Test
    fun `Given extra of another parcelable type and default When loadParcelable Then operation receives default`() {
        val activity = activityWithExtras(Bundle().apply { putParcelable("k", Other(1)) })
        var received: Item? = null

        activity.loadParcelable("k", Item(2)) { received = it }

        assertThat(received).isEqualTo(Item(2))
    }

    @Test
    fun `Given extra of another parcelable type and no default When loadParcelable Then operation is not invoked`() {
        val activity = activityWithExtras(Bundle().apply { putParcelable("k", Other(1)) })
        var invoked = false

        activity.loadParcelable<Item>("k") { invoked = true }

        assertThat(invoked).isFalse()
    }

    @Test
    fun `Given extras with parcelable When loadParcelable Then operation receives it`() {
        val activity = activityWithExtras(Bundle().apply { putParcelable("k", Item(1)) })
        var received: Item? = null

        activity.loadParcelable<Item>("k") { received = it }

        assertThat(received).isEqualTo(Item(1))
    }

    @Test
    fun `Given no extras and a default When loadParcelable Then operation receives default`() {
        val activity = Robolectric.buildActivity(Activity::class.java, Intent()).setup().get()
        var received: Item? = null

        activity.loadParcelable("k", Item(2)) { received = it }

        assertThat(received).isEqualTo(Item(2))
    }

    @Test
    fun `Given no value and no default When loadParcelable Then operation is not invoked`() {
        val activity = Robolectric.buildActivity(Activity::class.java, Intent()).setup().get()
        var invoked = false

        activity.loadParcelable<Item>("k") { invoked = true }

        assertThat(invoked).isFalse()
    }

    @Test
    fun `Given an activity When setResultOK Then result is RESULT_OK`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()

        activity.setResultOK()

        assertThat(shadowOf(activity).resultCode).isEqualTo(Activity.RESULT_OK)
        assertThat(activity.isFinishing).isFalse()
    }

    @Test
    fun `Given an activity When setResultOKAndFinish Then result is OK and activity finishes`() {
        val activity = Robolectric.buildActivity(Activity::class.java).setup().get()

        activity.setResultOKAndFinish()

        assertThat(shadowOf(activity).resultCode).isEqualTo(Activity.RESULT_OK)
        assertThat(activity.isFinishing).isTrue()
    }

    private fun activityWithExtras(
        extras: Bundle,
    ): Activity = Robolectric.buildActivity(Activity::class.java, Intent().putExtras(extras)).setup().get()

    @Parcelize
    data class Other(
        val id: Int,
    ) : Parcelable

    @Parcelize
    data class Item(
        val id: Int,
    ) : Parcelable
}
