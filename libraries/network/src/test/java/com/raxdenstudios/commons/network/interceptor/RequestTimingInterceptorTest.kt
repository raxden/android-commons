package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test

internal class RequestTimingInterceptorTest {

    @Test
    fun `intercept should call onRequestCompleted with url and duration`() {
        var capturedUrl: String? = null
        var capturedDuration: Long? = null
        val onRequestCompleted: (String, Long) -> Unit = { url, duration ->
            capturedUrl = url
            capturedDuration = duration
        }
        val interceptor = RequestTimingInterceptor(onRequestCompleted)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val url = "https://api.example.com/test".toHttpUrl()

        every { chain.request() } returns request
        every { request.url } returns url
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        assertThat(capturedUrl).isEqualTo("https://api.example.com/test")
        assertThat(capturedDuration).isNotNull()
        assertThat(capturedDuration).isAtLeast(0)
    }

    @Test
    fun `intercept should call onRequestCompleted even when request fails`() {
        var callbackInvoked = false
        val onRequestCompleted: (String, Long) -> Unit = { _, _ ->
            callbackInvoked = true
        }
        val interceptor = RequestTimingInterceptor(onRequestCompleted)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val url = "https://api.example.com/test".toHttpUrl()

        every { chain.request() } returns request
        every { request.url } returns url
        every { chain.proceed(request) } throws RuntimeException("Network error")

        try {
            interceptor.intercept(chain)
        } catch (e: RuntimeException) {
            // Expected
        }

        assertThat(callbackInvoked).isTrue()
    }

    @Test
    fun `intercept should measure actual request duration`() {
        var capturedDuration: Long? = null
        val onRequestCompleted: (String, Long) -> Unit = { _, duration ->
            capturedDuration = duration
        }
        val interceptor = RequestTimingInterceptor(onRequestCompleted)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val url = "https://api.example.com/test".toHttpUrl()

        every { chain.request() } returns request
        every { request.url } returns url
        every { chain.proceed(request) } answers {
            Thread.sleep(100)
            response
        }

        interceptor.intercept(chain)

        assertThat(capturedDuration).isNotNull()
        assertThat(capturedDuration).isAtLeast(100)
    }
}
