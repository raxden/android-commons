package com.raxdenstudios.retrofit.rx

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.Function
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor(
  private val callAdapter: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create(),
  private val errorHandler: ErrorHandler
) : CallAdapter.Factory() {

  companion object {
    fun create(errorHandler: ErrorHandler) = RxErrorHandlingCallAdapterFactory(
      errorHandler = errorHandler
    )
  }

  override fun get(
    returnType: Type,
    annotations: Array<Annotation>,
    retrofit: Retrofit
  ): CallAdapter<*, *>? = RxCallAdapterWrapper(
    retrofit,
    callAdapter.get(returnType, annotations, retrofit),
    errorHandler
  )

  private class RxCallAdapterWrapper<R>(
    private val retrofit: Retrofit,
    private val wrapped: CallAdapter<R, *>?,
    private val errorHandler: ErrorHandler
  ) : CallAdapter<R, Any> {

    override fun adapt(call: Call<R>): Any? {
      return when (val result = wrapped?.adapt(call)) {
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
    }

    private fun asRetrofitException(throwable: Throwable): Throwable {
      return try {
        when (throwable) {
          is HttpException -> httpError(throwable)
          is IOException -> networkError(throwable)
          else -> unexpectedError(throwable)
        }
      } catch (ioException: IOException) {
        networkError(ioException)
      }
    }

    private fun httpError(exception: HttpException): Throwable {
      val url = exception.response()?.raw()?.request?.url.toString()
      return exception.response()?.let { response ->
        when (response.code()) {
          401 -> unauthenticatedError(url, response)
          in 400..499 -> clientError(url, response)
          in 500..599 -> serverError(url, response)
          else -> unexpectedError(exception)
        }
      } ?: unexpectedError(exception)
    }

    private fun unauthenticatedError(url: String, it: Response<*>) =
      RetrofitException.unauthenticatedError(url, it, retrofit)

    private fun clientError(url: String, response: Response<*>): Throwable {
      val exception = RetrofitException.clientError(url, response, retrofit)
      return errorHandler.apiError(exception)
    }

    private fun serverError(url: String, response: Response<*>) =
      RetrofitException.serverError(url, response, retrofit)

    private fun networkError(exception: IOException) = RetrofitException.networkError(exception)

    private fun unexpectedError(throwable: Throwable) = RetrofitException.unexpectedError(throwable)

    override fun responseType(): Type? = wrapped?.responseType()
  }
}
