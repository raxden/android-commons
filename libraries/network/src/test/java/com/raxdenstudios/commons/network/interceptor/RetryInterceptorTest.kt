package com.raxdenstudios.commons.network.interceptor

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import okhttp3.Call
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Test
import java.io.IOException
import java.io.InterruptedIOException

internal class RetryInterceptorTest {

    private val call: Call = mockk {
        every { isCanceled() } returns false
    }
    private val request: Request = mockk {
        every { method } returns "GET"
    }
    private val chain: Interceptor.Chain = mockk {
        every { request() } returns request
        every { call() } returns call
    }

    @Test
    fun `intercept should return response on first successful attempt`() {
        val interceptor = RetryInterceptor.default
        val response = response(200)
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `intercept should retry on retryable response`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10)
        val failedResponse = response(503)
        val successResponse = response(200)
        every { chain.proceed(request) } returnsMany listOf(failedResponse, successResponse)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(successResponse)
        verify(exactly = 2) { chain.proceed(request) }
        verify { failedResponse.close() }
    }

    @Test
    fun `Given retryable codes, When intercept is called, Then each one is retried`() {
        listOf(500, 503, 408, 429).forEach { code ->
            val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 1)
            val success = response(200)
            every { chain.proceed(request) } returnsMany listOf(response(code), success)

            val result = interceptor.intercept(chain)

            assertThat(result).isEqualTo(success)
        }
    }

    @Test
    fun `Given non retryable codes, When intercept is called, Then only one attempt is made`() {
        listOf(400, 401, 404).forEach { code ->
            val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 1)
            val response = response(code)
            val localChain = chainReturning(response)

            val result = interceptor.intercept(localChain)

            assertThat(result).isEqualTo(response)
            verify(exactly = 1) { localChain.proceed(request) }
        }
    }

    @Test
    fun `Given a POST request, When intercept is called, Then only one attempt is made`() {
        every { request.method } returns "POST"
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 1)
        val response = response(503)
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 1) { chain.proceed(request) }
        verify(exactly = 0) { response.close() }
    }

    @Test
    fun `Given a POST request and retryNonIdempotent, When intercept is called, Then it is retried`() {
        every { request.method } returns "POST"
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 1, retryNonIdempotent = true)
        val success = response(200)
        every { chain.proceed(request) } returnsMany listOf(response(503), success)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(success)
        verify(exactly = 2) { chain.proceed(request) }
    }

    @Test
    fun `Given all attempts return 503, When intercept is called, Then last response is returned unclosed`() {
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 1)
        val first = response(503)
        val second = response(503)
        val last = response(503)
        every { chain.proceed(request) } returnsMany listOf(first, second, last)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(last)
        verify(exactly = 3) { chain.proceed(request) }
        verify(exactly = 1) { first.close() }
        verify(exactly = 1) { second.close() }
        verify(exactly = 0) { last.close() }
    }

    @Test
    fun `Given IOException on every attempt, When intercept is called, Then the last one is rethrown`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 1)
        every { chain.proceed(request) } throws IOException("first") andThenThrows IOException("last")

        val exception = assertThrows(IOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).hasMessageThat().isEqualTo("last")
        verify(exactly = 2) { chain.proceed(request) }
    }

    @Test
    fun `Given a 503 and then an IOException, When intercept is called, Then the IOException is rethrown`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 1)
        every { chain.proceed(request) } returns response(503) andThenThrows IOException("boom")

        val exception = assertThrows(IOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).hasMessageThat().isEqualTo("boom")
    }

    @Test
    fun `Given an IOException and then a success, When intercept is called, Then the success is returned`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 1)
        val success = response(200)
        every { chain.proceed(request) } throws IOException("boom") andThen success

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(success)
    }

    @Test
    fun `Given maxRetries zero, When intercept is called, Then proceed is called once`() {
        val interceptor = RetryInterceptor(maxRetries = 0, initialDelayMs = 1)
        val response = response(503)
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `Given a cancelled call, When proceed throws IOException, Then it is rethrown immediately`() {
        every { call.isCanceled() } returns true
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 1)
        every { chain.proceed(request) } throws IOException("Canceled")

        val exception = assertThrows(IOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).hasMessageThat().isEqualTo("Canceled")
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `Given a cancelled call, When proceed returns 503, Then it is returned without retry`() {
        every { call.isCanceled() } returns true
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 1)
        val response = response(503)
        every { chain.proceed(request) } returns response

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response)
        verify(exactly = 1) { chain.proceed(request) }
        verify(exactly = 0) { response.close() }
    }

    @Test(timeout = 5_000)
    fun `withMaxRetries should make that many attempts on 503`() {
        val interceptor = RetryInterceptor.withMaxRetries(5)
        every { chain.proceed(request) } answers { response(503, retryAfter = "0") }

        val result = interceptor.intercept(chain)

        assertThat(result.code).isEqualTo(503)
        verify(exactly = 5) { chain.proceed(request) }
    }

    @Test
    fun `Given maxRetries five, When every attempt returns 503, Then proceed is called five times`() {
        val interceptor = RetryInterceptor(maxRetries = 5, initialDelayMs = 0)
        every { chain.proceed(request) } answers { response(503) }

        val result = interceptor.intercept(chain)

        assertThat(result.code).isEqualTo(503)
        verify(exactly = 5) { chain.proceed(request) }
    }

    @Test
    fun `intercept should use custom shouldRetry predicate`() {
        val interceptor = RetryInterceptor(
            maxRetries = 2,
            initialDelayMs = 10,
            shouldRetry = { response -> response.code == 418 },
        )
        val response418 = response(418)
        val response200 = response(200)
        every { chain.proceed(request) } returnsMany listOf(response418, response200)

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(response200)
        verify(exactly = 2) { chain.proceed(request) }
    }

    @After
    fun clearInterruptFlag() {
        Thread.interrupted()
    }

    @Test(timeout = 5_000)
    fun `Given a 503 and a cancel in the wait, When intercept is called, Then it throws Canceled`() {
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 10_000)
        val failed = response(503)
        every { chain.proceed(request) } returns failed
        every { call.isCanceled() } returnsMany listOf(false, true)

        val exception = assertThrows(IOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).hasMessageThat().isEqualTo("Canceled")
        verify(exactly = 1) { failed.close() }
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test(timeout = 5_000)
    fun `Given an IOException and a cancel in the wait, When intercept is called, Then it is rethrown`() {
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 10_000)
        val original = IOException("net")
        every { chain.proceed(request) } throws original
        every { call.isCanceled() } returnsMany listOf(false, true)

        val exception = assertThrows(IOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).isSameInstanceAs(original)
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `Given a cancel right after a 503 attempt, When intercept is called, Then the response is returned unclosed`() {
        every { call.isCanceled() } returns true
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 10_000)
        val failed = response(503)
        every { chain.proceed(request) } returns failed

        val result = interceptor.intercept(chain)

        assertThat(result).isEqualTo(failed)
        verify(exactly = 0) { failed.close() }
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `Given an interrupted thread and a 503, When intercept is called, Then it throws InterruptedIOException`() {
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 10_000)
        val failed = response(503)
        every { chain.proceed(request) } returns failed
        Thread.currentThread().interrupt()

        val exception = assertThrows(InterruptedIOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).hasCauseThat().isInstanceOf(InterruptedException::class.java)
        assertThat(Thread.currentThread().isInterrupted).isTrue()
        verify(exactly = 1) { failed.close() }
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test
    fun `Given an interrupted thread and an IOException, When intercept is called, Then InterruptedIOException`() {
        val interceptor = RetryInterceptor(maxRetries = 3, initialDelayMs = 10_000)
        every { chain.proceed(request) } throws IOException("net")
        Thread.currentThread().interrupt()

        val exception = assertThrows(InterruptedIOException::class.java) { interceptor.intercept(chain) }

        assertThat(exception).hasCauseThat().isInstanceOf(InterruptedException::class.java)
        assertThat(Thread.currentThread().isInterrupted).isTrue()
        verify(exactly = 1) { chain.proceed(request) }
    }

    @Test(timeout = 5_000)
    fun `Given Retry-After 0 on 429 and 503, When intercept is called, Then it retries immediately`() {
        listOf(429, 503).forEach { code ->
            val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10_000)
            val success = response(200)
            every { chain.proceed(request) } returnsMany listOf(response(code, retryAfter = "0"), success)

            val result = interceptor.intercept(chain)

            assertThat(result).isEqualTo(success)
        }
    }

    @Test
    fun `Given Retry-After 0 on a 500, When intercept is called, Then the header is ignored and the backoff applies`() {
        val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10_000)
        every { chain.proceed(request) } returns response(500, retryAfter = "0")
        Thread.currentThread().interrupt()

        assertThrows(InterruptedIOException::class.java) { interceptor.intercept(chain) }
    }

    @Test
    fun `Given a non numeric or HTTP date Retry-After, When intercept is called, Then the backoff applies`() {
        listOf("soon", "Wed, 21 Oct 2015 07:28:00 GMT").forEach { header ->
            val interceptor = RetryInterceptor(maxRetries = 2, initialDelayMs = 10_000)
            every { chain.proceed(request) } returns response(503, retryAfter = header)
            Thread.currentThread().interrupt()

            assertThrows(InterruptedIOException::class.java) { interceptor.intercept(chain) }
            Thread.interrupted()
        }
    }

    @Test(timeout = 10_000)
    fun `Given many retries and a late cancel, When intercept is called, Then it ends without unexpected errors`() {
        val interceptor = RetryInterceptor(maxRetries = 40, initialDelayMs = 1)
        var checks = 0
        every { call.isCanceled() } answers { ++checks > 12 }
        every { chain.proceed(request) } answers { response(503) }

        val outcome = runCatching { interceptor.intercept(chain) }

        val error = outcome.exceptionOrNull()
        if (error != null) {
            assertThat(error).isInstanceOf(IOException::class.java)
            assertThat(error).hasMessageThat().isEqualTo("Canceled")
        }
        verify(atMost = 39) { chain.proceed(request) }
    }

    @Test(timeout = 5_000)
    fun `withDelay should retry using the given initial delay`() {
        val interceptor = RetryInterceptor.withDelay(0)
        every { chain.proceed(request) } answers { response(503) }

        val result = interceptor.intercept(chain)

        assertThat(result.code).isEqualTo(503)
        verify(exactly = 3) { chain.proceed(request) }
    }

    @Test(timeout = 5_000)
    fun `Given the default instance, When every attempt returns 503, Then it makes three attempts`() {
        every { chain.proceed(request) } answers { response(503, retryAfter = "0") }

        val result = RetryInterceptor.default.intercept(chain)

        assertThat(result.code).isEqualTo(503)
        verify(exactly = 3) { chain.proceed(request) }
    }

    private fun response(
        code: Int,
        retryAfter: String? = null,
    ): Response = mockk(relaxed = true) {
        every { this@mockk.code } returns code
        every { header("Retry-After") } returns retryAfter
    }

    private fun chainReturning(
        response: Response,
    ): Interceptor.Chain = mockk {
        every { request() } returns request
        every { call() } returns call
        every { proceed(request) } returns response
    }
}
