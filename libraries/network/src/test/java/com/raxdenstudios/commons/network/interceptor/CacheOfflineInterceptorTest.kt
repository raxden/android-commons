package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.network.interceptor.CacheOfflineInterceptor
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import java.util.concurrent.TimeUnit

internal class CacheOfflineInterceptorTest {

    @Test
    fun `default interceptor should use 7 days max stale`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val isNetworkAvailable = { true }

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptor = CacheOfflineInterceptor.default(isNetworkAvailable)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { chain.proceed(request) }
    }

    @Test
    fun `withMaxStale should create interceptor with custom max stale`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val isNetworkAvailable = { true }

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptor = CacheOfflineInterceptor.withMaxStale(14, isNetworkAvailable)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
    }

    @Test
    fun `intercept should not modify request when network is available`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val isNetworkAvailable = { true }
        val cacheControl = CacheControl.Builder()
            .maxStale(7, TimeUnit.DAYS)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptor = CacheOfflineInterceptor(cacheControl, isNetworkAvailable)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `intercept should modify request when network is not available`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val modifiedRequest = mockk<Request>()
        val response = mockk<Response>()
        val isNetworkAvailable = { false }
        val cacheControl = CacheControl.Builder()
            .maxStale(7, TimeUnit.DAYS)
            .build()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.removeHeader("Pragma") } returns requestBuilder
        every { requestBuilder.removeHeader("Cache-Control") } returns requestBuilder
        every { requestBuilder.cacheControl(cacheControl) } returns requestBuilder
        every { requestBuilder.build() } returns modifiedRequest
        every { chain.proceed(modifiedRequest) } returns response

        val interceptor = CacheOfflineInterceptor(cacheControl, isNetworkAvailable)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { requestBuilder.removeHeader("Pragma") }
        verify { requestBuilder.removeHeader("Cache-Control") }
        verify { requestBuilder.cacheControl(cacheControl) }
        verify { chain.proceed(modifiedRequest) }
    }

    @Test
    fun `intercept should use cache control when offline`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val modifiedRequest = mockk<Request>()
        val response = mockk<Response>()
        val isNetworkAvailable = { false }
        val cacheControl = CacheControl.Builder()
            .maxStale(14, TimeUnit.DAYS)
            .build()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.removeHeader(any()) } returns requestBuilder
        every { requestBuilder.cacheControl(cacheControl) } returns requestBuilder
        every { requestBuilder.build() } returns modifiedRequest
        every { chain.proceed(modifiedRequest) } returns response

        val interceptor = CacheOfflineInterceptor(cacheControl, isNetworkAvailable)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { requestBuilder.cacheControl(cacheControl) }
    }

    @Test
    fun `intercept should check network availability before modifying request`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        var networkCheckCount = 0
        val isNetworkAvailable = {
            networkCheckCount++
            true
        }
        val cacheControl = CacheControl.Builder()
            .maxStale(7, TimeUnit.DAYS)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val interceptor = CacheOfflineInterceptor(cacheControl, isNetworkAvailable)

        interceptor.intercept(chain)

        assertThat(networkCheckCount).isEqualTo(1)
    }
}
