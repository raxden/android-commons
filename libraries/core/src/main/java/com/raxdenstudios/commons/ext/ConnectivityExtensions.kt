package com.raxdenstudios.commons.ext

import android.content.Context
import android.net.ConnectivityManager
import com.raxdenstudios.commons.util.Network

fun Context.getConnectivityManager(): ConnectivityManager =
    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

fun Context.isNetworkConnected(): Boolean = Network.isNetworkConnected(this)
