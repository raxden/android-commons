package com.raxdenstudios.retrofit.rx

import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException

sealed class RetrofitException(
  error: Throwable?,
) : RuntimeException(error) {

  /**
   * A non-200 HTTP status code was received from the server.
   */
  data class Unauthenticated(
    val url: String,
    val response: Response<*>,
    val retrofit: Retrofit,
  ) : RetrofitException(null)

  /**
   * A non-200 HTTP status code was received from the server.
   */
  data class Client(
    val url: String,
    val response: Response<*>,
    val retrofit: Retrofit,
  ) : RetrofitException(null)

  /**
   * A non-200 HTTP status code was received from the server.
   */
  data class Server(
    val url: String,
    val response: Response<*>,
    val retrofit: Retrofit,
  ) : RetrofitException(null)

  /**
   * An [IOException] occurred while communicating to the server.
   */
  data class Network(
    val error: IOException
  ) : RetrofitException(error)

  /**
   * An internal error occurred while attempting to execute a request. It is best practice to
   * re-throw this exception so your application crashes.
   */
  data class Unexpected(
    val error: Throwable
  ) : RetrofitException(error)
}
