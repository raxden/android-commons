package com.raxdenstudios.commons.android.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
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
internal class NetworkTest {

    private val context: Context = mockk()
    private val manager: ConnectivityManager = mockk()
    private val network: android.net.Network = mockk()
    private val capabilities: NetworkCapabilities = mockk()

    @Before
    fun setUp() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns manager
        every { manager.activeNetwork } returns network
        every { manager.getNetworkCapabilities(network) } returns capabilities
        every { capabilities.hasTransport(any()) } returns false
        every { capabilities.hasCapability(any()) } returns false
    }

    @Test
    fun `Given no active network When checking connection Then returns false`() {
        every { manager.activeNetwork } returns null

        assertThat(Network.isNetworkConnected(context)).isFalse()
    }

    @Test
    fun `Given no capabilities When checking connection Then returns false`() {
        every { manager.getNetworkCapabilities(network) } returns null

        assertThat(Network.isNetworkConnected(context)).isFalse()
    }

    @Test
    fun `Given internet and validated capabilities When checking connection Then returns true`() {
        stubCapabilities(internet = true, validated = true)

        assertThat(Network.isNetworkConnected(context)).isTrue()
    }

    @Test
    fun `Given internet without validated When checking connection Then returns false`() {
        stubCapabilities(internet = true, validated = false)

        assertThat(Network.isNetworkConnected(context)).isFalse()
    }

    @Test
    fun `Given validated without internet When checking connection Then returns false`() {
        stubCapabilities(internet = false, validated = true)

        assertThat(Network.isNetworkConnected(context)).isFalse()
    }

    @Test
    fun `Given vpn transport with internet and validated When checking connection Then returns true`() {
        every { capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) } returns true
        stubCapabilities(internet = true, validated = true)

        assertThat(Network.isNetworkConnected(context)).isTrue()
    }

    private fun stubCapabilities(
        internet: Boolean,
        validated: Boolean,
    ) {
        every { capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns internet
        every { capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) } returns validated
    }
}
