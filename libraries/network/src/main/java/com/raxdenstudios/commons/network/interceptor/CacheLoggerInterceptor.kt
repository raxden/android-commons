package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class CacheLoggerInterceptor(
    private val printMessage: (message: String) -> Unit
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        response.networkResponse?.let { printMessage("Response from network") }
        response.cacheResponse?.let { printMessage("(HIT) Response from cache") }
        return response
    }
}
