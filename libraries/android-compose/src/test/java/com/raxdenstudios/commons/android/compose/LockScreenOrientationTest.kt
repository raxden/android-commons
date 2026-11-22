package com.raxdenstudios.commons.android.compose

import android.content.pm.ActivityInfo
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class LockScreenOrientationTest {

    private val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()

    @Test
    fun `Given Landscape, When composed, Then the activity is locked to landscape`() {
        activity.setContent { LockScreenOrientation(ScreenOrientation.Landscape) }
        idle()

        assertThat(activity.requestedOrientation).isEqualTo(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    }

    @Test
    fun `Given Portrait, When composed, Then the activity is locked to portrait`() {
        activity.setContent { LockScreenOrientation(ScreenOrientation.Portrait) }
        idle()

        assertThat(activity.requestedOrientation).isEqualTo(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    }

    @Test
    fun `Given Unspecified, When composed, Then the activity orientation is unspecified`() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        activity.setContent { LockScreenOrientation(ScreenOrientation.Unspecified) }
        idle()

        assertThat(activity.requestedOrientation).isEqualTo(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
    }

    @Test
    fun `Given a locked orientation, When the composable leaves, Then the previous orientation is restored`() {
        activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        var visible by mutableStateOf(true)
        activity.setContent { if (visible) LockScreenOrientation(ScreenOrientation.Landscape) }
        idle()
        assertThat(activity.requestedOrientation).isEqualTo(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        visible = false
        idle()

        assertThat(activity.requestedOrientation).isEqualTo(ActivityInfo.SCREEN_ORIENTATION_SENSOR)
    }

    private fun idle() {
        shadowOf(Looper.getMainLooper()).idle()
    }
}
