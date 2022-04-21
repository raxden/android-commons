package com.raxdenstudios.retrofit.rx

import com.raxdenstudios.commons.retrofit.RetrofitException
import okhttp3.ResponseBody
import retrofit2.Retrofit

@Suppress("ReturnCount")
inline fun <reified T> RetrofitException.parseError(): T? {
  val errorBody = errorBodyOrNull() ?: return null
  val retrofit = retrofitOrNull() ?: return null
  val converter = retrofit.responseBodyConverter<T>(T::class.java, arrayOfNulls(0))
  return converter.convert(errorBody)
}

fun RetrofitException.errorBodyOrNull(): ResponseBody? = when (this) {
  is RetrofitException.Non200Http -> response.errorBody()
  is RetrofitException.Network -> null
  is RetrofitException.Unexpected -> null
}

fun RetrofitException.retrofitOrNull(): Retrofit? = when (this) {
  is RetrofitException.Non200Http -> retrofit
  is RetrofitException.Network -> null
  is RetrofitException.Unexpected -> null
}
