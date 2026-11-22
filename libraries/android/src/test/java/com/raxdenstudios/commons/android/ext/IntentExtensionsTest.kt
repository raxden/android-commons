package com.raxdenstudios.commons.android.ext

import android.app.Application
import android.content.ContextWrapper
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import com.google.common.truth.Truth.assertThat
import kotlinx.parcelize.Parcelize
import org.junit.Assert.assertThrows
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
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

    @Test
    fun `Given no params When intentFor Then intent targets activity without extras`() {
        val intent = application.intentFor<android.app.Activity>()

        assertThat(intent.component?.className).isEqualTo(android.app.Activity::class.java.name)
        assertThat(intent.hasExtra("params")).isFalse()
    }

    @Test
    fun `Given params When intentFor Then params are stored as extra`() {
        val intent = application.intentFor<android.app.Activity>(Params(7))

        assertThat(intent.getParcelableExtra<Params>("params")).isEqualTo(Params(7))
    }

    @Test
    fun `Given an intent When startActivity Then it is started`() {
        val intent = Intent(application, android.app.Activity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.startActivity(application)

        assertThat(shadowOf(application).nextStartedActivity.component)
            .isEqualTo(intent.component)
    }

    @Test
    fun `Given an intent and options When startActivity Then it is started`() {
        val intent = Intent(application, android.app.Activity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.startActivity(application, Bundle())

        assertThat(shadowOf(application).nextStartedActivity).isNotNull()
    }

    @Test
    fun `Given an activity context When startActivityAndFinishCurrent Then starts and finishes it`() {
        val activity = Robolectric.buildActivity(android.app.Activity::class.java).setup().get()
        val intent = Intent(activity, android.app.Activity::class.java)

        intent.startActivityAndFinishCurrent(activity)

        assertThat(shadowOf(activity).nextStartedActivity).isNotNull()
        assertThat(activity.isFinishing).isTrue()
    }

    @Test
    fun `Given a non activity context When startActivity Then NEW_TASK flag is added`() {
        val intent = Intent(application, android.app.Activity::class.java)

        intent.startActivity(application)

        val started = shadowOf(application).nextStartedActivity
        assertThat(started.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isEqualTo(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @Test
    fun `Given a non activity context When startActivity Then the original intent flags are unchanged`() {
        val intent = Intent(application, android.app.Activity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

        intent.startActivity(application)

        val started = shadowOf(application).nextStartedActivity
        assertThat(intent.flags).isEqualTo(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        assertThat(started).isNotSameInstanceAs(intent)
        assertThat(started.flags and Intent.FLAG_ACTIVITY_NO_ANIMATION).isEqualTo(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }

    @Test
    fun `Given a context wrapper around application When startActivity Then NEW_TASK flag is added`() {
        val intent = Intent(application, android.app.Activity::class.java)

        intent.startActivity(ContextWrapper(application))

        val started = shadowOf(application).nextStartedActivity
        assertThat(started.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isEqualTo(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    @Test
    fun `Given an activity When startActivity Then NEW_TASK flag is not added`() {
        val activity = Robolectric.buildActivity(android.app.Activity::class.java).setup().get()
        val intent = Intent(activity, android.app.Activity::class.java)

        intent.startActivity(activity)

        val started = shadowOf(activity).nextStartedActivity
        assertThat(started.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isEqualTo(0)
    }

    @Test
    fun `Given a context wrapper around an activity When startActivity Then NEW_TASK flag is not added`() {
        val activity = Robolectric.buildActivity(android.app.Activity::class.java).setup().get()
        val intent = Intent(activity, android.app.Activity::class.java)

        intent.startActivity(ContextWrapper(activity))

        val started = shadowOf(activity).nextStartedActivity
        assertThat(started.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isEqualTo(0)
    }

    @Test
    fun `Given a context wrapper around an activity When startActivityAndFinishCurrent Then starts and finishes it`() {
        val activity = Robolectric.buildActivity(android.app.Activity::class.java).setup().get()
        val intent = Intent(activity, android.app.Activity::class.java)

        intent.startActivityAndFinishCurrent(ContextWrapper(activity))

        assertThat(shadowOf(activity).nextStartedActivity).isNotNull()
        assertThat(activity.isFinishing).isTrue()
    }

    @Test
    fun `Given a non activity context When startActivityAndFinishCurrent Then throws`() {
        val intent = Intent(application, android.app.Activity::class.java)

        assertThrows(IllegalArgumentException::class.java) {
            intent.startActivityAndFinishCurrent(application)
        }
    }

    @Parcelize
    data class Params(
        val id: Int,
    ) : Parcelable
}
