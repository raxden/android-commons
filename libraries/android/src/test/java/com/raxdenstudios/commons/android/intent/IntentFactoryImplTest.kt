package com.raxdenstudios.commons.android.intent

import android.content.Context
import android.content.Intent
import android.provider.Settings
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class IntentFactoryImplTest {

    private val context: Context = mockk()
    private lateinit var factory: IntentFactoryImpl

    @Before
    fun setUp() {
        every { context.packageName } returns PACKAGE_NAME
        factory = IntentFactoryImpl(context)
    }

    @Test
    fun `Given AppSettings, When build is called, Then return application details settings intent`() {
        val intent = factory.build(IntentType.AppSettings)

        assertThat(intent.action).isEqualTo(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        assertThat(intent.data.toString()).isEqualTo("package:$PACKAGE_NAME")
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    @Test
    fun `Given PlayStore, When build is called, Then return market view intent`() {
        val intent = factory.build(IntentType.PlayStore)

        assertThat(intent.action).isEqualTo(Intent.ACTION_VIEW)
        assertThat(intent.data.toString()).isEqualTo("market://details?id=$PACKAGE_NAME")
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    @Test
    fun `Given a debug package, When PlayStore is built, Then the debug suffix is stripped`() {
        every { context.packageName } returns "$PACKAGE_NAME.debug"

        val intent = factory.build(IntentType.PlayStore)

        assertThat(intent.data.toString()).isEqualTo("market://details?id=$PACKAGE_NAME")
    }

    @Test
    fun `Given debug in the middle of the package, When PlayStore is built, Then the package is unchanged`() {
        every { context.packageName } returns "com.debug.app"

        val intent = factory.build(IntentType.PlayStore)

        assertThat(intent.data.toString()).isEqualTo("market://details?id=com.debug.app")
    }

    @Test
    fun `Given ShareText, When build is called, Then return send intent with text and subject`() {
        val intent = factory.build(IntentType.ShareText(subject = "A subject", text = "Some text"))

        assertThat(intent.action).isEqualTo(Intent.ACTION_SEND)
        assertThat(intent.type).isEqualTo("text/plain")
        assertThat(intent.getStringExtra(Intent.EXTRA_TEXT)).isEqualTo("Some text")
        assertThat(intent.getStringExtra(Intent.EXTRA_SUBJECT)).isEqualTo("A subject")
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    @Test
    fun `Given NotificationSettings, When build is called, Then return app notification settings intent`() {
        val intent = factory.build(IntentType.NotificationSettings)

        assertThat(intent.action).isEqualTo(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
        assertThat(intent.getStringExtra(Settings.EXTRA_APP_PACKAGE)).isEqualTo(PACKAGE_NAME)
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    @Test
    fun `Given ExactAlarmSettings, When build is called, Then return schedule exact alarm intent`() {
        val intent = factory.build(IntentType.ExactAlarmSettings)

        assertThat(intent.action).isEqualTo(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
        assertThat(intent.data.toString()).isEqualTo("package:$PACKAGE_NAME")
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    @Test
    fun `Given FullScreenIntentSettings, When build is called, Then return full screen intent settings intent`() {
        val intent = factory.build(IntentType.FullScreenIntentSettings)

        assertThat(intent.action).isEqualTo(Settings.ACTION_MANAGE_APP_USE_FULL_SCREEN_INTENT)
        assertThat(intent.data.toString()).isEqualTo("package:$PACKAGE_NAME")
        assertThat(intent.flags and Intent.FLAG_ACTIVITY_NEW_TASK).isNotEqualTo(0)
    }

    private companion object {
        const val PACKAGE_NAME = "com.raxdenstudios.app"
    }
}
