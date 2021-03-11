package com.raxdenstudios.retrofit.rx

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

open class RetrofitException constructor(
  /** The request URL which produced the error.  */
  val url: String?,
  /** Response object containing status code, headers, body, etc.  */
  val response: Response<*>?,
  /** The event kind which triggered this error.  */
  val kind: Kind,
  val exception: Throwable?,
  /** The Retrofit this request was executed on  */
  val retrofit: Retrofit?
) : RuntimeException(exception) {

  /** Identifies the event kind which triggered a [RetrofitException].  */
  enum class Kind {
    /** An [IOException] occurred while communicating to the server.  */
    NETWORK,

    /** A non-200 HTTP status code was received from the server.  */
    UNAUTHENTICATED,
    CLIENT_ERROR,
    SERVER_ERROR,

    /**
     * An internal error occurred while attempting to execute a request. It is best practice to
     * re-throw this exception so your application crashes.
     */
    UNEXPECTED
  }

  companion object {

    fun unauthenticatedError(url: String, response: Response<*>, retrofit: Retrofit) =
      RetrofitException(url, response, Kind.UNAUTHENTICATED, null, retrofit)

    fun clientError(url: String, response: Response<*>, retrofit: Retrofit) =
      RetrofitException(url, response, Kind.CLIENT_ERROR, null, retrofit)

    fun serverError(url: String, response: Response<*>, retrofit: Retrofit) =
      RetrofitException(url, response, Kind.SERVER_ERROR, null, retrofit)

    fun networkError(exception: IOException) =
      RetrofitException(null, null, Kind.NETWORK, exception, null)

    fun unexpectedError(exception: Throwable) =
      RetrofitException(null, null, Kind.UNEXPECTED, exception, null)
  }
}
