package com.raxdenstudios.commons.android.ext

import android.app.Application
import android.content.Intent
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class IntentExtensionsTest {

    private val application: Application = RuntimeEnvironment.getApplication()

    @Test
    fun `Given an intent with flags, When finishing all is called, Then flags are kept plus NEW_TASK and CLEAR_TASK`() {
        val intent = Intent(application, android.app.Activity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        intent.startActivityAndFinishAll(application)

        val started = shadowOf(application).nextStartedActivity
        val expected = Intent.FLAG_ACTIVITY_NO_ANIMATION or
            Intent.FLAG_ACTIVITY_NEW_TASK or
            Intent.FLAG_ACTIVITY_CLEAR_TASK
        assertThat(started.flags and expected).isEqualTo(expected)
    }
}
