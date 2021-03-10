package com.raxdenstudios.commons.okhttp3

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CacheNetworkInterceptor : Interceptor {

  companion object {
    private const val HEADER_PRAGMA = "Pragma"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val MAX_AGE = 5
  }

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())

    val cacheControl = CacheControl.Builder()
      .maxAge(MAX_AGE, TimeUnit.SECONDS)
      .build()

    return response.newBuilder()
      .removeHeader(HEADER_PRAGMA)
      .removeHeader(HEADER_CACHE_CONTROL)
      .header(HEADER_CACHE_CONTROL, cacheControl.toString())
      .build()
  }
}

