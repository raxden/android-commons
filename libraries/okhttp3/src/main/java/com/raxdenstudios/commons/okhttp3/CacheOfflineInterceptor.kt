package com.raxdenstudios.commons.okhttp3

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CacheOfflineInterceptor(
  private val isNetworkAvailable: () -> Boolean
) : Interceptor {

  companion object {
    private const val HEADER_PRAGMA = "Pragma"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val MAX_STALE = 7
  }

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()

    if (!isNetworkAvailable()) {
      val cacheControl = CacheControl.Builder()
        .maxStale(MAX_STALE, TimeUnit.DAYS)
        .build()

      request = chain.request().newBuilder()
        .removeHeader(HEADER_PRAGMA)
        .removeHeader(HEADER_CACHE_CONTROL)
        .cacheControl(cacheControl)
        .build()
    }
    return chain.proceed(request)
  }
}
