package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

@Deprecated(
    message = "Will be removed in the next major version. " +
        "Implement a project-owned OkHttp Interceptor instead.",
    level = DeprecationLevel.WARNING,
)
class RequestTimingInterceptor(
    private val onRequestCompleted: (url: String, durationMs: Long) -> Unit,
) : Interceptor {

    @Suppress("TooGenericExceptionCaught")
    override fun intercept(
        chain: Interceptor.Chain,
    ): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()

        val response = try {
            chain.proceed(request)
        } finally {
            val duration = System.currentTimeMillis() - startTime
            onRequestCompleted(request.url.toString(), duration)
        }

        return response
    }
}
