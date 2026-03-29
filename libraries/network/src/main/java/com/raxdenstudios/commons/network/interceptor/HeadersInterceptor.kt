package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeadersInterceptor(
    private val headers: Map<String, String>
) : Interceptor {

    companion object {
        fun withUserAgent(userAgent: String) = HeadersInterceptor(
            mapOf("User-Agent" to userAgent)
        )

        fun withApiVersion(version: String) = HeadersInterceptor(
            mapOf("X-API-Version" to version)
        )

        fun withHeaders(vararg pairs: Pair<String, String>) = HeadersInterceptor(
            mapOf(*pairs)
        )
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        headers.forEach { (key, value) ->
            requestBuilder.header(key, value)
        }
        return chain.proceed(requestBuilder.build())
    }
}
