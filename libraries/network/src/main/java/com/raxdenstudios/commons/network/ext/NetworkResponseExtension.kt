package com.raxdenstudios.commons.network.ext

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.core.NetworkError
import com.raxdenstudios.commons.core.ResultData

fun <S : Any, E : Any> NetworkResponse<S, E>.toResultData(
    errorMessage: String,
): ResultData<S, NetworkError<E>> = when (this) {
    is NetworkResponse.Success -> ResultData.Success(body)
    is NetworkResponse.ServerError -> toResultData(errorMessage)
    is NetworkResponse.NetworkError -> toResultData(errorMessage)
    is NetworkResponse.UnknownError -> toResultData(errorMessage)
}

@Suppress("MagicNumber")
private fun <S : Any, E : Any> NetworkResponse.ServerError<S, E>.toResultData(
    message: String
): ResultData<S, NetworkError<E>> {
    val code = code ?: -1
    return code.toResultData(null, body, message)
}

private fun <S : Any, E : Any> NetworkResponse.NetworkError<S, E>.toResultData(
    message: String
): ResultData<S, NetworkError<E>> = ResultData.Failure(
    NetworkError.Network(message = message)
)

private fun <S : Any, E : Any> NetworkResponse.UnknownError<S, E>.toResultData(
    message: String
): ResultData<S, NetworkError<E>> = ResultData.Failure(
    NetworkError.Unknown(code, body, message)
)
