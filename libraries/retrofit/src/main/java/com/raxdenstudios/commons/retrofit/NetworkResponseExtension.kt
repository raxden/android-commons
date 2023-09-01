package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.commons.ResultData

@Suppress("MagicNumber")
inline fun <S : Any, E : Any, reified R : Any> NetworkResponse<S, E>.toResultData(
    errorMessage: String,
    onSuccess: (value: S) -> R = { value -> value as R },
): ResultData<R, NetworkError<E>> = when (this) {
    is NetworkResponse.Success -> ResultData.Success(onSuccess(body))
    is NetworkResponse.ServerError -> {
        val networkError = when (val code = code ?: -1) {
            in (400..499) -> NetworkError.Client(
                code = code,
                message = errorMessage,
                body = body
            )

            in (500..599) -> NetworkError.Server(
                code = code,
                message = errorMessage,
                body = body
            )

            else -> NetworkError.Unknown(
                code = code,
                message = errorMessage,
                body = body
            )
        }
        ResultData.Failure(networkError)
    }

    is NetworkResponse.NetworkError -> ResultData.Failure(
        NetworkError.Network(message = errorMessage)
    )

    is NetworkResponse.UnknownError -> ResultData.Failure(
        NetworkError.Unknown(code, body, errorMessage)
    )
}
