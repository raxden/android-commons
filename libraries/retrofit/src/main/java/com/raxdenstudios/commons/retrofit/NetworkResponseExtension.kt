package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.android.ResultData

fun <T : Any, U : Any, R : Any> NetworkResponse<T, U>.toResult(
  message: String,
  transform: (value: T) -> R
): ResultData<R> = when (this) {
  is NetworkResponse.Success -> ResultData.Success(transform(body))
  is NetworkResponse.ServerError -> when (code) {
    in 400..499 -> ResultData.Error(NetworkException.Client(code, message))
    in 500..599 -> ResultData.Error(NetworkException.Server(code, message))
    else -> ResultData.Error(NetworkException.Unknown(message))
  }
  is NetworkResponse.NetworkError -> ResultData.Error(NetworkException.Network(message, error))
  is NetworkResponse.UnknownError -> ResultData.Error(NetworkException.Unknown(message, error))
}
