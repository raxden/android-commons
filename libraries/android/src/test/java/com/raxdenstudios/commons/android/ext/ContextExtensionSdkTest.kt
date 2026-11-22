package com.raxdenstudios.commons.android.ext

import android.app.AlarmManager
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
internal class ContextExtensionSdkTest {

    private val context: Context = mockk()
    private val packageManager: PackageManager = mockk()
    private val packageInfo = PackageInfo()

    @Before
    fun setUp() {
        every { context.packageName } returns PACKAGE_NAME
        every { context.packageManager } returns packageManager
    }

    @Test
    @Config(sdk = [33])
    fun `Given API 33, When getPackageInfo is called, Then use the PackageInfoFlags overload`() {
        every {
            packageManager.getPackageInfo(PACKAGE_NAME, any<PackageManager.PackageInfoFlags>())
        } returns packageInfo

        val result = context.getPackageInfo()

        assertThat(result).isSameInstanceAs(packageInfo)
        verify { packageManager.getPackageInfo(PACKAGE_NAME, any<PackageManager.PackageInfoFlags>()) }
    }

    @Test
    @Config(sdk = [32])
    fun `Given API below 33, When getPackageInfo is called, Then use the legacy int overload`() {
        every { packageManager.getPackageInfo(PACKAGE_NAME, 0) } returns packageInfo

        val result = context.getPackageInfo()

        assertThat(result).isSameInstanceAs(packageInfo)
        verify { packageManager.getPackageInfo(PACKAGE_NAME, 0) }
    }

    @Test
    @Config(sdk = [30])
    fun `Given API below 31, When isExactAlarmAllowed is called, Then return true without asking the system`() {
        assertThat(context.isExactAlarmAllowed()).isTrue()
    }

    @Test
    @Config(sdk = [31])
    fun `Given API 31 and exact alarms allowed, When isExactAlarmAllowed is called, Then return true`() {
        val alarmManager: AlarmManager = mockk()
        every { context.getSystemService(AlarmManager::class.java) } returns alarmManager
        every { alarmManager.canScheduleExactAlarms() } returns true

        assertThat(context.isExactAlarmAllowed()).isTrue()
    }

    @Test
    @Config(sdk = [31])
    fun `Given API 31 and exact alarms denied, When isExactAlarmAllowed is called, Then return false`() {
        val alarmManager: AlarmManager = mockk()
        every { context.getSystemService(AlarmManager::class.java) } returns alarmManager
        every { alarmManager.canScheduleExactAlarms() } returns false

        assertThat(context.isExactAlarmAllowed()).isFalse()
    }

    @Test
    @Config(sdk = [33])
    fun `Given API below 34, When isFullScreenIntentAllowed is called, Then return true without asking the system`() {
        assertThat(context.isFullScreenIntentAllowed()).isTrue()
    }

    @Test
    @Config(sdk = [34])
    fun `Given API 34 and full screen intents allowed, When isFullScreenIntentAllowed is called, Then return true`() {
        val notificationManager: NotificationManager = mockk()
        every { context.getSystemService(NotificationManager::class.java) } returns notificationManager
        every { notificationManager.canUseFullScreenIntent() } returns true

        assertThat(context.isFullScreenIntentAllowed()).isTrue()
    }

    @Test
    @Config(sdk = [34])
    fun `Given API 34 and full screen intents denied, When isFullScreenIntentAllowed is called, Then return false`() {
        val notificationManager: NotificationManager = mockk()
        every { context.getSystemService(NotificationManager::class.java) } returns notificationManager
        every { notificationManager.canUseFullScreenIntent() } returns false

        assertThat(context.isFullScreenIntentAllowed()).isFalse()
    }

    private companion object {
        const val PACKAGE_NAME = "com.raxdenstudios.app"
    }
}
