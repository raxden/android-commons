package com.raxdenstudios.commons.kotlin

sealed class ResultData<out T> {

  data class Success<out T>(val value: T) : ResultData<T>()
  data class Error(val throwable: Throwable) : ResultData<Nothing>()

  override fun toString(): String =
    when (this) {
      is Success<*> -> "Success[data=$value]"
      is Error -> "Error[throwable=$throwable]"
    }
}

fun <T, R> ResultData<T>.map(transform: (value: T) -> R): ResultData<R> = when (this) {
  is ResultData.Error -> ResultData.Error(throwable)
  is ResultData.Success -> ResultData.Success(transform(value))
}

fun <T> ResultData<T>.getValue(): T? = when (this) {
  is ResultData.Error -> null
  is ResultData.Success -> value
}
