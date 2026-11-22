package com.raxdenstudios.commons.android.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [27])
internal class SDKLegacyVersionCodeTest {

    private val context: Context = mockk()
    private val packageManager: PackageManager = mockk()

    @Test
    fun `Given package info below P When getting version code Then returns versionCode`() {
        every { context.packageManager } returns packageManager
        every { context.packageName } returns "com.test"
        every { packageManager.getPackageInfo("com.test", 0) } returns
            PackageInfo().apply { versionCode = 42 }

        assertThat(SDK.getVersionCode(context)).isEqualTo(42L)
    }

    @Test
    fun `Given package manager failure below P When getting version code Then returns zero`() {
        every { context.packageManager } returns packageManager
        every { context.packageName } returns "com.test"
        every { packageManager.getPackageInfo("com.test", 0) } throws
            PackageManager.NameNotFoundException()

        assertThat(SDK.getVersionCode(context)).isEqualTo(0L)
    }
}
