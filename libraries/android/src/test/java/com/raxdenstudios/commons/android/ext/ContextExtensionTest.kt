package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ContextExtensionTest {

    private val context: Context = mockk(relaxed = true)
    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)
    private val network: Network = mockk(relaxed = true)
    private val networkCapabilities: NetworkCapabilities = mockk(relaxed = true)

    @Test
    fun `isNetworkConnected should return true`() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager
        every { connectivityManager.activeNetwork } returns network
        every { connectivityManager.getNetworkCapabilities(network) } returns networkCapabilities
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true
        every { networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) } returns true

        val result = context.isNetworkConnected()

        assertTrue(result)
    }

    @Test
    fun `getConnectivityManager should return valid data`() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        val result = context.getConnectivityManager()

        assertEquals(connectivityManager, result)
    }

    @Test
    fun `getPackageInfo should return valid data`() {
        val result = context.getPackageInfo()

        assertEquals(
            context.packageManager.getPackageInfo(context.packageName, 0),
            result,
        )
    }

    @Test
    fun `findActivity should return valid data`() {
        val context = mockk<Activity>()

        val result = context.findActivity()

        assertTrue(result != null)
    }

    @Test
    fun `given a context, when findActivity is called Then return valid data`() {
        val context = mockk<ContextWrapper>()
        every { context.baseContext } returns mockk<Activity>()

        val result = context.findActivity()

        assertTrue(result != null)
    }

    @Test
    fun `Given a release package, When releasePackageName is called, Then return it unchanged`() {
        every { context.packageName } returns "com.app"

        assertEquals("com.app", context.releasePackageName())
    }

    @Test
    fun `Given a debug package, When releasePackageName is called, Then strip the debug suffix`() {
        every { context.packageName } returns "com.app.debug"

        assertEquals("com.app", context.releasePackageName())
    }

    @Test
    fun `Given a custom suffix, When releasePackageName is called, Then strip that suffix`() {
        every { context.packageName } returns "com.app.dev"

        assertEquals("com.app", context.releasePackageName(debugSuffix = ".dev"))
    }

    @Test
    fun `Given debug in the middle of the package, When releasePackageName is called, Then return it unchanged`() {
        every { context.packageName } returns "com.debug.app"

        assertEquals("com.debug.app", context.releasePackageName())
    }
}
