package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData

@Suppress("MagicNumber")
inline fun <T : Any, U : Any, reified R : Any> NetworkResponse<T, U>.toResultData(
    errorMessage: String,
    transformSuccess: (value: T) -> R = { value -> value as R },
): ResultData<R, NetworkError<U>> = when (this) {
    is NetworkResponse.Success -> ResultData.Success(transformSuccess(body))
    is NetworkResponse.ServerError -> {
        val error = when (val code = code ?: -1) {
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
        ResultData.Failure(error)
    }
    is NetworkResponse.NetworkError -> ResultData.Failure(
        NetworkError.Network(
            message = errorMessage,
        )
    )
    is NetworkResponse.UnknownError -> ResultData.Failure(
        NetworkError.Unknown(
            code = code,
            message = errorMessage,
            body = body
        )
    )
}
