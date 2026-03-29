package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import java.util.concurrent.TimeUnit

internal class CacheNetworkInterceptorTest {

    @Test
    fun `default interceptor should use 5 seconds max age`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBuilder = mockk<Response.Builder>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.newBuilder() } returns responseBuilder
        every { responseBuilder.removeHeader("Pragma") } returns responseBuilder
        every { responseBuilder.removeHeader("Cache-Control") } returns responseBuilder
        every { responseBuilder.header("Cache-Control", any()) } returns responseBuilder
        every { responseBuilder.build() } returns response

        val interceptor = CacheNetworkInterceptor.default

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { responseBuilder.removeHeader("Pragma") }
        verify { responseBuilder.removeHeader("Cache-Control") }
        verify { responseBuilder.header("Cache-Control", any()) }
    }

    @Test
    fun `withMaxAge should create interceptor with custom max age`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBuilder = mockk<Response.Builder>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.newBuilder() } returns responseBuilder
        every { responseBuilder.removeHeader("Pragma") } returns responseBuilder
        every { responseBuilder.removeHeader("Cache-Control") } returns responseBuilder
        every { responseBuilder.header("Cache-Control", any()) } returns responseBuilder
        every { responseBuilder.build() } returns response

        val interceptor = CacheNetworkInterceptor.withMaxAge(10)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { responseBuilder.header("Cache-Control", match { it.contains("max-age=10") }) }
    }

    @Test
    fun `intercept should remove Pragma and Cache-Control headers`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBuilder = mockk<Response.Builder>()
        val cacheControl = CacheControl.Builder()
            .maxAge(5, TimeUnit.SECONDS)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.newBuilder() } returns responseBuilder
        every { responseBuilder.removeHeader("Pragma") } returns responseBuilder
        every { responseBuilder.removeHeader("Cache-Control") } returns responseBuilder
        every { responseBuilder.header("Cache-Control", any()) } returns responseBuilder
        every { responseBuilder.build() } returns response

        val interceptor = CacheNetworkInterceptor(cacheControl)

        interceptor.intercept(chain)

        verify(exactly = 1) { responseBuilder.removeHeader("Pragma") }
        verify(exactly = 1) { responseBuilder.removeHeader("Cache-Control") }
    }

    @Test
    fun `intercept should add Cache-Control header with correct value`() {
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()
        val responseBuilder = mockk<Response.Builder>()
        val cacheControl = CacheControl.Builder()
            .maxAge(30, TimeUnit.SECONDS)
            .build()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.newBuilder() } returns responseBuilder
        every { responseBuilder.removeHeader(any()) } returns responseBuilder
        every { responseBuilder.header("Cache-Control", cacheControl.toString()) } returns responseBuilder
        every { responseBuilder.build() } returns response

        val interceptor = CacheNetworkInterceptor(cacheControl)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { responseBuilder.header("Cache-Control", cacheControl.toString()) }
    }
}
