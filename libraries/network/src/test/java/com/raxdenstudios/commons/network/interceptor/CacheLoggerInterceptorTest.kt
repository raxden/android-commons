package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test

internal class CacheLoggerInterceptorTest {

    private val printMessage: (String) -> Unit = mockk(relaxed = true)
    private val interceptor = CacheLoggerInterceptor(printMessage)

    @Test
    fun `intercept should log network response when response is from network`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response> {
            every { networkResponse } returns mockk()
            every { cacheResponse } returns null
        }

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { printMessage("Response from network") }
    }

    @Test
    fun `intercept should log cache hit when response is from cache`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response> {
            every { networkResponse } returns null
            every { cacheResponse } returns mockk()
        }

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { printMessage("(HIT) Response from cache") }
    }

    @Test
    fun `intercept should log both when response is from network and cache`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response> {
            every { networkResponse } returns mockk()
            every { cacheResponse } returns mockk()
        }

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { printMessage("Response from network") }
        verify { printMessage("(HIT) Response from cache") }
    }

    @Test
    fun `intercept should not log when response is neither from network nor cache`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response> {
            every { networkResponse } returns null
            every { cacheResponse } returns null
        }

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 0) { printMessage(any()) }
    }
}
