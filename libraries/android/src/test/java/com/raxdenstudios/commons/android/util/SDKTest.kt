package com.raxdenstudios.commons.android.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.util.ReflectionHelpers

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class SDKTest {

    private val context: Context = mockk()
    private val resources: Resources = mockk()
    private val packageManager: PackageManager = mockk()

    private val originalFingerprint: String = Build.FINGERPRINT
    private val originalModel: String = Build.MODEL
    private val originalManufacturer: String = Build.MANUFACTURER
    private val originalBrand: String = Build.BRAND
    private val originalDevice: String = Build.DEVICE
    private val originalProduct: String = Build.PRODUCT

    @Before
    fun setUp() {
        every { context.resources } returns resources
        every { context.packageManager } returns packageManager
        every { context.packageName } returns "com.test"
        setBuild("FINGERPRINT", "real/fingerprint")
        setBuild("MODEL", "Pixel")
        setBuild("MANUFACTURER", "Google")
        setBuild("BRAND", "google")
        setBuild("DEVICE", "pixel")
        setBuild("PRODUCT", "pixel")
    }

    @After
    fun tearDown() {
        setBuild("FINGERPRINT", originalFingerprint)
        setBuild("MODEL", originalModel)
        setBuild("MANUFACTURER", originalManufacturer)
        setBuild("BRAND", originalBrand)
        setBuild("DEVICE", originalDevice)
        setBuild("PRODUCT", originalProduct)
    }

    private fun setBuild(
        field: String,
        value: String,
    ) {
        ReflectionHelpers.setStaticField(Build::class.java, field, value)
    }

    @Test
    fun `Given a real device When checking emulator Then returns false`() {
        assertThat(SDK.isEmulator).isFalse()
    }

    @Test
    fun `Given generic fingerprint When checking emulator Then returns true`() {
        setBuild("FINGERPRINT", "generic/sdk")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given unknown fingerprint When checking emulator Then returns true`() {
        setBuild("FINGERPRINT", "unknown")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given google_sdk model When checking emulator Then returns true`() {
        setBuild("MODEL", "google_sdk")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given Emulator model When checking emulator Then returns true`() {
        setBuild("MODEL", "Android Emulator")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given x86 sdk model When checking emulator Then returns true`() {
        setBuild("MODEL", "Android SDK built for x86")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given Genymotion manufacturer When checking emulator Then returns true`() {
        setBuild("MANUFACTURER", "Genymotion")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given generic brand and device When checking emulator Then returns true`() {
        setBuild("BRAND", "generic")
        setBuild("DEVICE", "generic_x86")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given generic brand but real device When checking emulator Then returns false`() {
        setBuild("BRAND", "generic")

        assertThat(SDK.isEmulator).isFalse()
    }

    @Test
    fun `Given google_sdk product When checking emulator Then returns true`() {
        setBuild("PRODUCT", "google_sdk")

        assertThat(SDK.isEmulator).isTrue()
    }

    @Test
    fun `Given navigation bar config true When checking virtual bar Then returns true`() {
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 1
        every { resources.getBoolean(1) } returns true

        assertThat(SDK.hasVirtualNavigationBar(context)).isTrue()
    }

    @Test
    fun `Given navigation bar config false on real device When checking virtual bar Then returns false`() {
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 1
        every { resources.getBoolean(1) } returns false

        assertThat(SDK.hasVirtualNavigationBar(context)).isFalse()
    }

    @Test
    fun `Given missing navigation bar resource on real device When checking virtual bar Then returns false`() {
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 0

        assertThat(SDK.hasVirtualNavigationBar(context)).isFalse()
    }

    @Test
    fun `Given missing navigation bar resource on emulator When checking virtual bar Then returns true`() {
        setBuild("FINGERPRINT", "generic")
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 0

        assertThat(SDK.hasVirtualNavigationBar(context)).isTrue()
    }

    @Test
    fun `Given no virtual bar When getting its height Then returns zero`() {
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 0

        assertThat(SDK.getVirtualNavigationBarHeight(context)).isEqualTo(0)
    }

    @Test
    fun `Given virtual bar and dimen resource When getting its height Then returns the dimension`() {
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 1
        every { resources.getBoolean(1) } returns true
        every { resources.getIdentifier("navigation_bar_height", "dimen", "android") } returns 2
        every { resources.getDimensionPixelSize(2) } returns 48

        assertThat(SDK.getVirtualNavigationBarHeight(context)).isEqualTo(48)
    }

    @Test
    fun `Given virtual bar and no dimen resource When getting its height Then returns zero`() {
        every { resources.getIdentifier("config_showNavigationBar", "bool", "android") } returns 1
        every { resources.getBoolean(1) } returns true
        every { resources.getIdentifier("navigation_bar_height", "dimen", "android") } returns 0

        assertThat(SDK.getVirtualNavigationBarHeight(context)).isEqualTo(0)
    }

    @Test
    fun `Given status bar dimen resource When getting its height Then returns the dimension`() {
        every { resources.getIdentifier("status_bar_height", "dimen", "android") } returns 3
        every { resources.getDimensionPixelSize(3) } returns 24

        assertThat(SDK.getStatusBarHeight(context)).isEqualTo(24)
    }

    @Test
    fun `Given no status bar dimen resource When getting its height Then returns zero`() {
        every { resources.getIdentifier("status_bar_height", "dimen", "android") } returns 0

        assertThat(SDK.getStatusBarHeight(context)).isEqualTo(0)
    }

    @Test
    fun `Given package info When getting package name Then returns it`() {
        stubPackageInfo(PackageInfo().apply { packageName = "com.test" })

        assertThat(SDK.getPackageName(context)).isEqualTo("com.test")
    }

    @Test
    fun `Given package manager failure When getting package name Then returns empty`() {
        stubPackageInfoFailure()

        assertThat(SDK.getPackageName(context)).isEmpty()
    }

    @Test
    fun `Given package info When getting version name Then returns it`() {
        stubPackageInfo(PackageInfo().apply { versionName = "1.2.3" })

        assertThat(SDK.getVersionName(context)).isEqualTo("1.2.3")
    }

    @Test
    fun `Given null version name When getting version name Then returns empty`() {
        stubPackageInfo(PackageInfo().apply { versionName = null })

        assertThat(SDK.getVersionName(context)).isEmpty()
    }

    @Test
    fun `Given package manager failure When getting version name Then returns empty`() {
        stubPackageInfoFailure()

        assertThat(SDK.getVersionName(context)).isEmpty()
    }

    @Test
    fun `Given package info on P or above When getting version code Then returns longVersionCode`() {
        stubPackageInfo(PackageInfo().apply { longVersionCode = 5_000_000_000L })

        assertThat(SDK.getVersionCode(context)).isEqualTo(5_000_000_000L)
    }

    @Test
    fun `Given package manager failure on P or above When getting version code Then returns zero`() {
        stubPackageInfoFailure()

        assertThat(SDK.getVersionCode(context)).isEqualTo(0L)
    }

    private fun stubPackageInfo(
        info: PackageInfo,
    ) {
        every {
            packageManager.getPackageInfo("com.test", any<PackageManager.PackageInfoFlags>())
        } returns info
        every { packageManager.getPackageInfo("com.test", 0) } returns info
    }

    private fun stubPackageInfoFailure() {
        every {
            packageManager.getPackageInfo("com.test", any<PackageManager.PackageInfoFlags>())
        } throws PackageManager.NameNotFoundException()
        every {
            packageManager.getPackageInfo("com.test", 0)
        } throws PackageManager.NameNotFoundException()
    }
}
