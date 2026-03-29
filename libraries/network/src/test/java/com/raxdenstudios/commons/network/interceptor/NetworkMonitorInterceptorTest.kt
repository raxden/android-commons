package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test

internal class NetworkMonitorInterceptorTest {

    @Test
    fun `intercept should proceed when network is available`() {
        val isNetworkAvailable = { true }
        val interceptor = NetworkMonitorInterceptor(isNetworkAvailable)

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
    fun `intercept should throw NoNetworkException when network is not available`() {
        val isNetworkAvailable = { false }
        val interceptor = NetworkMonitorInterceptor(isNetworkAvailable)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()

        every { chain.request() } returns request

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected NoNetworkException to be thrown")
        } catch (e: NoNetworkException) {
            assertThat(e.message).isEqualTo("No network connection available")
        }

        verify(exactly = 0) { chain.proceed(any()) }
    }

    @Test
    fun `intercept should check network availability before proceeding`() {
        var networkCheckCount = 0
        val isNetworkAvailable = {
            networkCheckCount++
            true
        }
        val interceptor = NetworkMonitorInterceptor(isNetworkAvailable)

        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response

        interceptor.intercept(chain)

        assertThat(networkCheckCount).isEqualTo(1)
    }
}
