package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test

internal class HeadersInterceptorTest {

    @Test
    fun `intercept should add all headers to request`() {
        val headers = mapOf(
            "X-Custom-Header" to "custom-value",
            "X-Another-Header" to "another-value"
        )
        val interceptor = HeadersInterceptor(headers)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val modifiedRequest = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.header(any(), any()) } returns requestBuilder
        every { requestBuilder.build() } returns modifiedRequest
        every { chain.proceed(modifiedRequest) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { requestBuilder.header("X-Custom-Header", "custom-value") }
        verify { requestBuilder.header("X-Another-Header", "another-value") }
        verify { chain.proceed(modifiedRequest) }
    }

    @Test
    fun `withUserAgent should create interceptor with User-Agent header`() {
        val userAgent = "MyApp/1.0"
        val interceptor = HeadersInterceptor.withUserAgent(userAgent)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val modifiedRequest = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.header("User-Agent", userAgent) } returns requestBuilder
        every { requestBuilder.build() } returns modifiedRequest
        every { chain.proceed(modifiedRequest) } returns response

        interceptor.intercept(chain)

        verify { requestBuilder.header("User-Agent", userAgent) }
    }

    @Test
    fun `withApiVersion should create interceptor with X-API-Version header`() {
        val version = "v2"
        val interceptor = HeadersInterceptor.withApiVersion(version)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val modifiedRequest = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.header("X-API-Version", version) } returns requestBuilder
        every { requestBuilder.build() } returns modifiedRequest
        every { chain.proceed(modifiedRequest) } returns response

        interceptor.intercept(chain)

        verify { requestBuilder.header("X-API-Version", version) }
    }

    @Test
    fun `withHeaders should create interceptor with multiple headers`() {
        val interceptor = HeadersInterceptor.withHeaders(
            "Header1" to "Value1",
            "Header2" to "Value2"
        )

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val modifiedRequest = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.header(any(), any()) } returns requestBuilder
        every { requestBuilder.build() } returns modifiedRequest
        every { chain.proceed(modifiedRequest) } returns response

        interceptor.intercept(chain)

        verify { requestBuilder.header("Header1", "Value1") }
        verify { requestBuilder.header("Header2", "Value2") }
    }
}
