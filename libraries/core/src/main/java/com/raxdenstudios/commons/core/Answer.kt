package com.raxdenstudios.commons.core

sealed class Answer<out T, out E> {

    data class Success<out T>(val value: T) : Answer<T, Nothing>()
    data class Failure<out E>(val value: E) : Answer<Nothing, E>()

    val isSuccess get() = this is Success
    val isFailure get() = this is Failure
}
