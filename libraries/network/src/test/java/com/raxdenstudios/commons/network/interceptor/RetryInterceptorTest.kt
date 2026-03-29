package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.Test
import java.io.IOException

internal class RetryInterceptorTest {

    @Test
    fun `intercept should return response on first successful attempt`() {
        val interceptor = RetryInterceptor.default
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns true

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `intercept should retry on unsuccessful response`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val failedResponse = mockk<Response>()
        val successResponse = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returnsMany listOf(failedResponse, successResponse)
        every { failedResponse.isSuccessful } returns false
        every { failedResponse.close() } returns Unit
        every { successResponse.isSuccessful } returns true

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(successResponse)
        verify(exactly = 2) { chain.proceed(request) }
        verify { failedResponse.close() }
    }

    @Test
    fun `intercept should throw IOException when max retries exceeded`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.close() } returns Unit

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected IOException to be thrown")
        } catch (e: IOException) {
            assertThat(e.message).isEqualTo("Max retries exceeded")
        }

        verify(exactly = 2) { chain.proceed(request) }
    }

    @Test
    fun `intercept should retry on IOException`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } throwsMany listOf(
            IOException("Network error"),
            IOException("Network error")
        ) andThen response
        every { response.isSuccessful } returns true

        try {
            interceptor.intercept(chain)
            throw AssertionError("Expected IOException to be thrown")
        } catch (e: IOException) {
            assertThat(e.message).isEqualTo("Network error")
        }

        verify(exactly = 2) { chain.proceed(request) }
    }

    @Test
    fun `withMaxRetries should create interceptor with custom max retries`() {
        val interceptor = RetryInterceptor.withMaxRetries(5)
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returns response
        every { response.isSuccessful } returns false
        every { response.close() } returns Unit

        try {
            interceptor.intercept(chain)
        } catch (e: IOException) {
            // Expected
        }

        verify(exactly = 5) { chain.proceed(request) }
    }

    @Test
    fun `intercept should use custom shouldRetry predicate`() {
        val shouldRetry: (Response) -> Boolean = { response ->
            response.code == 503
        }
        val interceptor = RetryInterceptor(
            maxRetries = 2,
            initialDelayMs = 10,
            shouldRetry = shouldRetry
        )
        val chain = mockk<Interceptor.Chain>()
        val request = mockk<Request>()
        val response503 = mockk<Response>()
        val response200 = mockk<Response>()

        every { chain.request() } returns request
        every { chain.proceed(request) } returnsMany listOf(response503, response200)
        every { response503.code } returns 503
        every { response503.close() } returns Unit
        every { response200.code } returns 200

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response200)
        verify(exactly = 2) { chain.proceed(request) }
    }
}
