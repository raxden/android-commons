package com.raxdenstudios.commons.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.raxdenstudios.commons.ext.getConnectivityManager

object Network {

  fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = context.getConnectivityManager()
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      isNetworkConnectedAPI23(connectivityManager)
    } else isNetworkConnectedLegacy(connectivityManager)
  }

  @Suppress("DEPRECATION")
  @SuppressLint("MissingPermission")
  private fun isNetworkConnectedLegacy(manager: ConnectivityManager): Boolean {
    val activeNetwork = manager.activeNetworkInfo ?: return false
    return when (activeNetwork.type) {
      ConnectivityManager.TYPE_WIFI -> true
      ConnectivityManager.TYPE_MOBILE -> true
      ConnectivityManager.TYPE_ETHERNET -> true
      else -> false
    }
  }

  @RequiresApi(Build.VERSION_CODES.M)
  @SuppressLint("MissingPermission")
  private fun isNetworkConnectedAPI23(manager: ConnectivityManager): Boolean {
    val activeNetwork = manager.activeNetwork ?: return false
    return isNetworkConnectedAPI23(manager, activeNetwork)
  }

  @SuppressLint("MissingPermission")
  private fun isNetworkConnectedAPI23(
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
