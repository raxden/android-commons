package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.commons.ResultData

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
    val networkError = when (val code = code ?: -1) {
        in (400..499) -> NetworkError.Client(
            code = code,
            message = message,
            body = body
        )

        in (500..599) -> NetworkError.Server(
            code = code,
            message = message,
            body = body
        )

        else -> NetworkError.Unknown(
            code = code,
            message = message,
            body = body
        )
    }
    return ResultData.Failure(networkError)
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
