package com.raxdenstudios.commons.network

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.network.interceptor.CacheLoggerInterceptor
import com.raxdenstudios.commons.network.interceptor.CacheNetworkInterceptor
import com.raxdenstudios.commons.network.interceptor.CacheOfflineInterceptor
import com.raxdenstudios.commons.network.interceptor.RetryInterceptor
import io.mockk.mockk
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Test
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.time.Duration.Companion.seconds

internal class DefaultHttpClientFactoryTest {

    private val retryInterceptor: RetryInterceptor = mockk(relaxed = true)
    private val loggingInterceptor: HttpLoggingInterceptor = mockk(relaxed = true)
    private val cacheOfflineInterceptor: CacheOfflineInterceptor = mockk(relaxed = true)
    private val cacheNetworkInterceptor: CacheNetworkInterceptor = mockk(relaxed = true)
    private val cacheLoggerInterceptor: CacheLoggerInterceptor = mockk(relaxed = true)
    private val customInterceptor: Interceptor = mockk(relaxed = true)
    private val customNetworkInterceptor: Interceptor = mockk(relaxed = true)

    @Test
    fun `Given default configuration, When create is called, Then default timeouts are applied`() {
        val client = DefaultHttpClientFactory().create()

        assertThat(client.readTimeoutMillis).isEqualTo(35_000)
        assertThat(client.writeTimeoutMillis).isEqualTo(35_000)
        assertThat(client.connectTimeoutMillis).isEqualTo(15_000)
    }

    @Test
    fun `Given default configuration, When create is called, Then no interceptors nor cache and retry enabled`() {
        val client = DefaultHttpClientFactory().create()

        assertThat(client.interceptors).isEmpty()
        assertThat(client.networkInterceptors).isEmpty()
        assertThat(client.cache).isNull()
        assertThat(client.retryOnConnectionFailure).isTrue()
    }

    @Test
    fun `Given custom timeouts, When create is called, Then they are applied`() {
        val factory = DefaultHttpClientFactory(
            readTimeout = 1.seconds,
            writeTimeout = 2.seconds,
            connectTimeout = 3.seconds,
        )

        val client = factory.create()

        assertThat(client.readTimeoutMillis).isEqualTo(1_000)
        assertThat(client.writeTimeoutMillis).isEqualTo(2_000)
        assertThat(client.connectTimeoutMillis).isEqualTo(3_000)
    }

    @Test
    fun `Given all interceptors, When create is called, Then application interceptors keep the documented order`() {
        val factory = DefaultHttpClientFactory(
            retryInterceptor = retryInterceptor,
            httpLoggingInterceptor = loggingInterceptor,
            cacheOfflineInterceptor = cacheOfflineInterceptor,
            interceptors = listOf(customInterceptor),
        )

        val client = factory.create()

        assertThat(client.interceptors)
            .containsExactly(customInterceptor, retryInterceptor, loggingInterceptor, cacheOfflineInterceptor)
            .inOrder()
    }

    @Test
    fun `Given network interceptors, When create is called, Then they are added in order after the cache ones`() {
        val factory = DefaultHttpClientFactory(
            cacheNetworkInterceptor = cacheNetworkInterceptor,
            cacheLoggerInterceptor = cacheLoggerInterceptor,
            networkInterceptors = listOf(customNetworkInterceptor),
        )

        val client = factory.create()

        assertThat(client.networkInterceptors)
            .containsExactly(cacheNetworkInterceptor, cacheLoggerInterceptor, customNetworkInterceptor)
            .inOrder()
        assertThat(client.interceptors).isEmpty()
    }

    @Test
    fun `Given only some interceptors, When create is called, Then null ones are skipped`() {
        val factory = DefaultHttpClientFactory(
            retryInterceptor = retryInterceptor,
            cacheLoggerInterceptor = cacheLoggerInterceptor,
        )

        val client = factory.create()

        assertThat(client.interceptors).containsExactly(retryInterceptor)
        assertThat(client.networkInterceptors).containsExactly(cacheLoggerInterceptor)
    }

    @Test
    fun `Given a cache, When create is called, Then the client uses it`() {
        val cache: Cache = mockk(relaxed = true)

        val client = DefaultHttpClientFactory(cache = cache).create()

        assertThat(client.cache).isSameInstanceAs(cache)
    }

    @Test
    fun `Given retryOnConnectionFailure false, When create is called, Then it is disabled`() {
        val client = DefaultHttpClientFactory(retryOnConnectionFailure = false).create()

        assertThat(client.retryOnConnectionFailure).isFalse()
    }

    @Test
    fun `Given an SSL socket factory, When create is called, Then it is used and configuration is kept`() {
        val sslSocketFactory: SSLSocketFactory = mockk(relaxed = true)
        val trustManager: X509TrustManager = mockk(relaxed = true)
        val factory = DefaultHttpClientFactory(
            retryInterceptor = retryInterceptor,
            readTimeout = 5.seconds,
        )

        val client = factory.create(sslSocketFactory, trustManager)

        assertThat(client.sslSocketFactory).isSameInstanceAs(sslSocketFactory)
        assertThat(client.interceptors).containsExactly(retryInterceptor)
        assertThat(client.readTimeoutMillis).isEqualTo(5_000)
    }
}
