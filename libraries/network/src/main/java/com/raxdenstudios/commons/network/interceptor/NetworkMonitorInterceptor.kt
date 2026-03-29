package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkMonitorInterceptor(
    private val isNetworkAvailable: () -> Boolean
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isNetworkAvailable()) {
            throw NoNetworkException("No network connection available")
        }
        return chain.proceed(chain.request())
    }
}

class NoNetworkException(message: String) : IOException(message)
