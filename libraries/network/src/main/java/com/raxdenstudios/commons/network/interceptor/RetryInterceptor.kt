package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RetryInterceptor(
    private val maxRetries: Int = 3,
    private val initialDelayMs: Long = 1000,
    private val shouldRetry: (Response) -> Boolean = { !it.isSuccessful }
) : Interceptor {

    companion object {
        val default = RetryInterceptor()

        fun withMaxRetries(retries: Int) = RetryInterceptor(maxRetries = retries)

        fun withDelay(delayMs: Long) = RetryInterceptor(initialDelayMs = delayMs)
    }

    @Suppress("TooGenericExceptionCaught")
    override fun intercept(chain: Interceptor.Chain): Response {
        var attempt = 0
        var response: Response? = null
        var exception: IOException? = null

        while (attempt < maxRetries) {
            try {
                response = chain.proceed(chain.request())

                if (!shouldRetry(response)) {
                    return response
                }

                response.close()
            } catch (e: IOException) {
                exception = e
            }

            attempt++
            if (attempt < maxRetries) {
                Thread.sleep(initialDelayMs * (1 shl (attempt - 1)))
            }
        }

        throw exception ?: IOException("Max retries exceeded")
    }
}
