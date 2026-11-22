package com.raxdenstudios.commons.network

import com.raxdenstudios.commons.network.interceptor.CacheLoggerInterceptor
import com.raxdenstudios.commons.network.interceptor.CacheNetworkInterceptor
import com.raxdenstudios.commons.network.interceptor.CacheOfflineInterceptor
import com.raxdenstudios.commons.network.interceptor.RetryInterceptor
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * Builds configured [OkHttpClient] instances.
 *
 * Every call builds a new client, so create one per API (or share the result) instead of calling it per request.
 * OkHttp clients are meant to be reused, because they own connection pools and thread pools.
 *
 * ## Usage
 *
 * ```kotlin
 * val factory: HttpClientFactory = DefaultHttpClientFactory(
 *     retryInterceptor = RetryInterceptor(maxRetries = 3),
 *     interceptors = listOf(AuthTokenInterceptor { token }),
 * )
 * val client: OkHttpClient = factory.create()
 * ```
 */
interface HttpClientFactory {

    /** Creates a client with the factory configuration, using the platform default TLS settings. */
    fun create(): OkHttpClient

    /**
     * Creates a client with the factory configuration and the given [sslSocketFactory] and [x509TrustManager],
     * for custom TLS such as certificate pinning or a private CA.
     */
    fun create(
        sslSocketFactory: SSLSocketFactory,
        x509TrustManager: X509TrustManager,
    ): OkHttpClient
}

/**
 * [HttpClientFactory] that assembles the client from the optional interceptors and timeouts passed in.
 *
 * Interceptors are added in this order. Application interceptors run for every call, before the cache
 * check, in this order: [interceptors] (in list order), [retryInterceptor], [httpLoggingInterceptor],
 * [cacheOfflineInterceptor]. Network interceptors run only when the request reaches the network, in this
 * order: [cacheNetworkInterceptor], [cacheLoggerInterceptor], then [networkInterceptors] (in list order).
 * Null parameters are skipped, and [cache] becomes the client cache. [networkInterceptors] come after the
 * cache network and logger interceptors, so they run after those on the request and before them on the response.
 * [readTimeout], [writeTimeout] and [connectTimeout] apply to every call made by the client.
 */
class DefaultHttpClientFactory(
    private val retryInterceptor: RetryInterceptor? = null,
    private val httpLoggingInterceptor: HttpLoggingInterceptor? = null,
    private val cacheOfflineInterceptor: CacheOfflineInterceptor? = null,
    private val cacheNetworkInterceptor: CacheNetworkInterceptor? = null,
    private val cacheLoggerInterceptor: CacheLoggerInterceptor? = null,
    private val cache: Cache? = null,
    private val interceptors: List<Interceptor> = emptyList(),
    private val networkInterceptors: List<Interceptor> = emptyList(),
    private val retryOnConnectionFailure: Boolean = true,
    private val readTimeout: Duration = DEFAULT_TIMEOUT,
    private val writeTimeout: Duration = DEFAULT_TIMEOUT,
    private val connectTimeout: Duration = DEFAULT_CONNECT_TIMEOUT,
) : HttpClientFactory {

    override fun create(): OkHttpClient = initOkHttpClientBuilder()
        .build()

    override fun create(
        sslSocketFactory: SSLSocketFactory,
        x509TrustManager: X509TrustManager,
    ): OkHttpClient = initOkHttpClientBuilder()
        .sslSocketFactory(sslSocketFactory, x509TrustManager)
        .build()

    private fun initOkHttpClientBuilder(): OkHttpClient.Builder = OkHttpClient.Builder().apply {
        cache(cache)
        // Application Interceptors (executed before cache check)
        interceptors.forEach { interceptor -> addInterceptor(interceptor) }
        retryInterceptor?.let { interceptor -> addInterceptor(interceptor) }
        httpLoggingInterceptor?.let { interceptor -> addInterceptor(interceptor) }
        cacheOfflineInterceptor?.let { interceptor -> addInterceptor(interceptor) }
        // Network Interceptors (executed only on network calls)
        cacheNetworkInterceptor?.let { interceptor -> addNetworkInterceptor(interceptor) }
        cacheLoggerInterceptor?.let { interceptor -> addNetworkInterceptor(interceptor) }
        networkInterceptors.forEach { interceptor -> addNetworkInterceptor(interceptor) }
        retryOnConnectionFailure(retryOnConnectionFailure)
        readTimeout(readTimeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        writeTimeout(writeTimeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
        connectTimeout(connectTimeout.inWholeMilliseconds, TimeUnit.MILLISECONDS)
    }

    companion object {

        /** Default read and write timeout. */
        val DEFAULT_TIMEOUT: Duration = 35.seconds

        /** Default connect timeout. */
        val DEFAULT_CONNECT_TIMEOUT: Duration = 15.seconds
    }
}
