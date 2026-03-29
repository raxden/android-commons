package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test

internal class AuthTokenInterceptorTest {

    @Test
    fun `intercept should add Authorization header when token is available`() {
        val token = "test-token-123"
        val tokenProvider = { token }
        val interceptor = AuthTokenInterceptor(tokenProvider)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val authorizedRequest = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.header("Authorization", "Bearer $token") } returns requestBuilder
        every { requestBuilder.build() } returns authorizedRequest
        every { chain.proceed(authorizedRequest) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { requestBuilder.header("Authorization", "Bearer $token") }
        verify { chain.proceed(authorizedRequest) }
    }

    @Test
    fun `intercept should not add Authorization header when token is null`() {
        val tokenProvider = { null }
        val interceptor = AuthTokenInterceptor(tokenProvider)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify { chain.proceed(request) }
    }

    @Test
    fun `intercept should use latest token from provider`() {
        var token: String? = "initial-token"
        val tokenProvider = { token }
        val interceptor = AuthTokenInterceptor(tokenProvider)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val requestBuilder = mockk<Request.Builder>()
        val authorizedRequest = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { request.newBuilder() } returns requestBuilder
        every { requestBuilder.header("Authorization", any()) } returns requestBuilder
        every { requestBuilder.build() } returns authorizedRequest
        every { chain.proceed(authorizedRequest) } returns response

        interceptor.intercept(chain)
        verify { requestBuilder.header("Authorization", "Bearer initial-token") }

        token = "updated-token"
        interceptor.intercept(chain)
        verify { requestBuilder.header("Authorization", "Bearer updated-token") }
    }
}
