package com.raxdenstudios.commons.retrofit.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

// TODO -> CUSTOM LOGGER
class CacheLoggerInterceptor : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())
    if (response.networkResponse != null)
      printMessage("Response from network")
    if (response.cacheResponse != null)
      printMessage("(HIT) Response from cache")
    return response
  }

  private fun printMessage(message: String) {
    Timber.tag("OkHttp").d(message)
  }
}
