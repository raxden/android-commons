package com.raxdenstudios.commons.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.raxdenstudios.commons.android.ext.getConnectivityManager

object Network {

    /**
     * Returns whether the active network currently provides validated internet access.
     *
     * True only when the active network has [NetworkCapabilities.NET_CAPABILITY_INTERNET] and
     * [NetworkCapabilities.NET_CAPABILITY_VALIDATED], on any transport (Wi-Fi, cellular, Ethernet, VPN).
     * False for captive portals, and for a network that has just connected until Android validates it.
     * Requires the `ACCESS_NETWORK_STATE` permission.
     */
    fun isNetworkConnected(
        context: Context,
    ): Boolean {
        val connectivityManager = context.getConnectivityManager()
        return isNetworkConnected(connectivityManager)
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkConnected(
        manager: ConnectivityManager,
    ): Boolean {
        val activeNetwork = manager.activeNetwork ?: return false
        return isNetworkConnected(manager, activeNetwork)
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkConnected(
        manager: ConnectivityManager,
        network: android.net.Network,
    ): Boolean {
        val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }
}
