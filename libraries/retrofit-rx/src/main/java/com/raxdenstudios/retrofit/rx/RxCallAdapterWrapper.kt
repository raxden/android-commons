package com.raxdenstudios.retrofit.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Call
import io.reactivex.functions.Function
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

internal class RxCallAdapterWrapper<R>(
  private val retrofit: Retrofit,
  private val wrapped: CallAdapter<R, *>?,
  private val errorHandler: ErrorHandler
) : CallAdapter<R, Any> {

  override fun responseType(): Type? = wrapped?.responseType()

  override fun adapt(call: Call<R>): Any? =
    when (val result = wrapped?.adapt(call)) {
      is Single<*> -> result.onErrorResumeNext(
        Function { throwable -> Single.error(asRetrofitException(throwable)) }
      )
      is Maybe<*> -> result.onErrorResumeNext(
        Function { throwable -> Maybe.error(asRetrofitException(throwable)) }
      )
      is Completable -> result.onErrorResumeNext {
        Completable.error(asRetrofitException(it))
      }
      is Flowable<*> -> result.onErrorResumeNext(
        Function { Flowable.error(asRetrofitException(it)) }
      )
      is Observable<*> -> result.onErrorResumeNext(
        Function { Observable.error(asRetrofitException(it)) }
      )
      else -> result
    }

  private fun asRetrofitException(throwable: Throwable): Throwable {
    return try {
      when (throwable) {
        is HttpException -> httpError(throwable)
        is IOException -> RetrofitException.Network(throwable)
        else -> RetrofitException.Unexpected(throwable)
      }
    } catch (ioException: IOException) {
      RetrofitException.Network(ioException)
    }
  }

  private fun httpError(exception: HttpException): Throwable {
    val url = exception.response()?.raw()?.request?.url.toString()
    return exception.response()?.let { response ->
      when (response.code()) {
        401 -> RetrofitException.Non200Http.Unauthenticated(url, response, retrofit)
        in 400..499 -> clientError(url, response)
        in 500..599 -> RetrofitException.Non200Http.Server(url, response, retrofit)
        else -> RetrofitException.Unexpected(exception)
      }
    } ?: RetrofitException.Unexpected(exception)
  }

  private fun clientError(url: String, response: Response<*>): Throwable {
    val exception = RetrofitException.Non200Http.Client(url, response, retrofit)
    return errorHandler.httpError(exception)
  }
}
