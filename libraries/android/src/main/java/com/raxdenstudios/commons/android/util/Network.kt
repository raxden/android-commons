package com.raxdenstudios.commons.android.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.raxdenstudios.commons.android.ext.getConnectivityManager

object Network {

    fun isNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getConnectivityManager()
        return isNetworkConnected(connectivityManager)
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkConnected(manager: ConnectivityManager): Boolean {
        val activeNetwork = manager.activeNetwork ?: return false
        return isNetworkConnected(manager, activeNetwork)
    }

    @SuppressLint("MissingPermission")
    private fun isNetworkConnected(
        manager: ConnectivityManager,
        network: android.net.Network
    ): Boolean {
        val networkCapabilities = manager.getNetworkCapabilities(network) ?: return false
        return when {
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}
