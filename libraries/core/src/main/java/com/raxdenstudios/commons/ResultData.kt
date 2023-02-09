package com.raxdenstudios.commons

sealed class ResultData<out T, out E> {

    data class Success<out T>(val value: T) : ResultData<T, Nothing>()
    data class Failure<out E>(val value: E) : ResultData<Nothing, E>()
}

