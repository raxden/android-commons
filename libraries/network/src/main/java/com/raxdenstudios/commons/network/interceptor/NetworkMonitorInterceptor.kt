package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Implement a project-owned OkHttp Interceptor instead.",
    level = DeprecationLevel.WARNING,
)
class NetworkMonitorInterceptor(
    private val isNetworkAvailable: () -> Boolean,
) : Interceptor {

    override fun intercept(
        chain: Interceptor.Chain,
    ): Response {
        if (!isNetworkAvailable()) {
            throw NoNetworkException("No network connection available")
        }
        return chain.proceed(chain.request())
    }
}

class NoNetworkException(
    message: String,
) : IOException(message)
