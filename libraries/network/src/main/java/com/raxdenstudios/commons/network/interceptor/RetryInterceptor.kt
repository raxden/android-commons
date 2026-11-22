package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.io.InterruptedIOException

/**
 * Retries a request up to [maxRetries] attempts in total (at least one), waiting
 * [initialDelayMs] doubled after each failed attempt.
 *
 * An attempt fails when it throws an [IOException] or when [shouldRetry] returns `true` for its
 * response; by default only 408, 429 and 5xx responses are retried. Only idempotent methods
 * (GET, HEAD, OPTIONS, PUT, DELETE) are retried unless [retryNonIdempotent] is `true`.
 *
 * The wait is capped at 60 seconds (or [initialDelayMs] if larger), and a 429 or 503 response
 * with a `Retry-After` header in seconds waits that long instead. Retrying stops as soon as the
 * call is cancelled. A failed response is closed before waiting, so no connection is held
 * during the wait. If the call is cancelled while waiting, an [IOException] ("Canceled") is
 * thrown after a failed response, or the [IOException] of the failed attempt is rethrown. When no
 * attempt is left, or the call is cancelled right after an attempt, the last response is returned
 * as is, or the last [IOException] is rethrown. An interrupt while waiting throws
 * [InterruptedIOException] and keeps the thread's interrupt flag set.
 */
class RetryInterceptor(
    private val maxRetries: Int = DEFAULT_MAX_RETRIES,
    private val initialDelayMs: Long = DEFAULT_INITIAL_DELAY_MS,
    private val shouldRetry: (Response) -> Boolean = ::isRetryableResponse,
    private val retryNonIdempotent: Boolean = false,
) : Interceptor {

    /** Kept for binary compatibility with callers compiled before [retryNonIdempotent] existed. */
    @Deprecated("Kept for binary compatibility", level = DeprecationLevel.HIDDEN)
    constructor(
        maxRetries: Int = DEFAULT_MAX_RETRIES,
        initialDelayMs: Long = DEFAULT_INITIAL_DELAY_MS,
        shouldRetry: (Response) -> Boolean = ::isRetryableResponse,
    ) : this(maxRetries, initialDelayMs, shouldRetry, retryNonIdempotent = false)

    /** Never cap below [initialDelayMs], so a caller asking for long delays keeps them. */
    private val maxDelayMs = maxOf(initialDelayMs, MAX_DELAY_MS)

    companion object {
        val default = RetryInterceptor()

        fun withMaxRetries(
            retries: Int,
        ) = RetryInterceptor(maxRetries = retries)

        fun withDelay(
            delayMs: Long,
        ) = RetryInterceptor(initialDelayMs = delayMs)
    }

    override fun intercept(
        chain: Interceptor.Chain,
    ): Response {
        val request = chain.request()
        val maxAttempts = if (retryNonIdempotent || request.method in IDEMPOTENT_METHODS) {
            maxRetries.coerceAtLeast(1)
        } else {
            1
        }
        var attempt = 0
        while (true) {
            attempt++
            val isLastAttempt = attempt >= maxAttempts
            val response = try {
                chain.proceed(request)
            } catch (e: IOException) {
                val canRetry = !isLastAttempt && !chain.call().isCanceled() && awaitRetry(chain, backoffDelay(attempt))
                if (!canRetry) throw e
                null
            }
            if (response != null) {
                if (isLastAttempt || chain.call().isCanceled() || !shouldRetry(response)) return response
                val delayMs = retryDelay(response, attempt)
                response.close()
                if (!awaitRetry(chain, delayMs)) throw IOException("Canceled")
            }
        }
    }

    /**
     * Sleeps [delayMs] in short slices and returns `false` as soon as the call is cancelled.
     * An interrupt restores the thread's interrupt flag and throws [InterruptedIOException].
     */
    private fun awaitRetry(
        chain: Interceptor.Chain,
        delayMs: Long,
    ): Boolean {
        var remainingMs = delayMs
        try {
            while (remainingMs > 0 && !chain.call().isCanceled()) {
                val sliceMs = remainingMs.coerceAtMost(SLEEP_SLICE_MS)
                Thread.sleep(sliceMs)
                remainingMs -= sliceMs
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
            throw InterruptedIOException("Interrupted while waiting to retry").apply { initCause(e) }
        }
        return !chain.call().isCanceled()
    }

    /** Uses the `Retry-After` seconds of a 429 or 503 response when present, else [backoffDelay]. */
    private fun retryDelay(
        response: Response,
        attempt: Int,
    ): Long {
        val honoursRetryAfter = response.code == HTTP_TOO_MANY_REQUESTS || response.code == HTTP_UNAVAILABLE
        val retryAfterMs = if (honoursRetryAfter) response.retryAfterMs() else null
        return (retryAfterMs ?: backoffDelay(attempt)).coerceAtMost(maxDelayMs)
    }

    /** [initialDelayMs] doubled per completed [attempt], saturating instead of overflowing, capped at [maxDelayMs]. */
    private fun backoffDelay(
        attempt: Int,
    ): Long {
        val shift = (attempt - 1).coerceIn(0, MAX_BACKOFF_SHIFT)
        val delayMs = if (initialDelayMs > Long.MAX_VALUE shr shift) Long.MAX_VALUE else initialDelayMs shl shift
        return delayMs.coerceAtMost(maxDelayMs)
    }
}

/** The `Retry-After` header in milliseconds when it holds non-negative seconds; HTTP dates are ignored. */
private fun Response.retryAfterMs(): Long? {
    val seconds = header(RETRY_AFTER_HEADER)?.trim()?.toLongOrNull()?.takeIf { it >= 0 } ?: return null
    return if (seconds > Long.MAX_VALUE / MILLIS_IN_SECOND) Long.MAX_VALUE else seconds * MILLIS_IN_SECOND
}

private fun isRetryableResponse(
    response: Response,
): Boolean = response.code == HTTP_REQUEST_TIMEOUT ||
    response.code == HTTP_TOO_MANY_REQUESTS ||
    response.code in HTTP_SERVER_ERRORS

private val IDEMPOTENT_METHODS = setOf("GET", "HEAD", "OPTIONS", "PUT", "DELETE")
private val HTTP_SERVER_ERRORS = 500..599
private const val HTTP_REQUEST_TIMEOUT = 408
private const val HTTP_TOO_MANY_REQUESTS = 429
private const val HTTP_UNAVAILABLE = 503
private const val RETRY_AFTER_HEADER = "Retry-After"
private const val MILLIS_IN_SECOND = 1000L
private const val SLEEP_SLICE_MS = 100L
private const val MAX_DELAY_MS = 60_000L
private const val MAX_BACKOFF_SHIFT = 30
private const val DEFAULT_MAX_RETRIES = 3
private const val DEFAULT_INITIAL_DELAY_MS = 1000L
