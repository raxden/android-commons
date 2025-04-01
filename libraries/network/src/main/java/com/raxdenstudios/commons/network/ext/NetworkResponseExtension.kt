package com.raxdenstudios.commons.network.ext

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.core.Answer
import com.raxdenstudios.commons.core.NetworkError

fun <S : Any, E : Any> NetworkResponse<S, E>.toAnswer(
    errorMessage: String,
): Answer<S, NetworkError<E>> = when (this) {
    is NetworkResponse.Success -> Answer.Success(body)
    is NetworkResponse.ServerError -> toAnswer(errorMessage)
    is NetworkResponse.NetworkError -> toAnswer(errorMessage)
    is NetworkResponse.UnknownError -> toAnswer(errorMessage)
}

@Suppress("MagicNumber")
private fun <S : Any, E : Any> NetworkResponse.ServerError<S, E>.toAnswer(
    message: String
): Answer<S, NetworkError<E>> {
    val code = code ?: -1
    return code.toAnswer(null, body, message)
}

private fun <S : Any, E : Any> NetworkResponse.NetworkError<S, E>.toAnswer(
    message: String
): Answer<S, NetworkError<E>> = Answer.Failure(
    NetworkError.Network(message = message)
)

private fun <S : Any, E : Any> NetworkResponse.UnknownError<S, E>.toAnswer(
    message: String
): Answer<S, NetworkError<E>> = Answer.Failure(
    NetworkError.Unknown(code, body, message)
)
