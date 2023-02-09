package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData

@Suppress("MagicNumber")
inline fun <T : Any, U : Any, reified R : Any, reified E : Any> NetworkResponse<T, U>.toResultData(
    errorMessage: String,
    transformSuccess: (value: T) -> R = { value -> value as R },
    transformFailure: (value: U) -> E = { value -> value as E },
): ResultData<R, NetworkError> = when (this) {
    is NetworkResponse.Success -> ResultData.Success(transformSuccess(body))
    is NetworkResponse.ServerError -> {
        val error = when (val code = code ?: -1) {
            in (400..499) -> NetworkError.Client(
                code = code,
                message = errorMessage,
                body = body?.let { transformFailure(it) }
            )
            in (500..599) -> NetworkError.Server(
                code = code,
                message = errorMessage,
                body = body?.let { transformFailure(it) }
            )
            else -> NetworkError.Unknown(
                code = code,
                message = errorMessage,
                body = body?.let { transformFailure(it) }
            )
        }
        ResultData.Failure(error)
    }
    is NetworkResponse.NetworkError -> ResultData.Failure(NetworkError.Network(errorMessage))
    is NetworkResponse.UnknownError -> ResultData.Failure(
        NetworkError.Unknown(
            code = code,
            message = errorMessage,
            body = body?.let { transformFailure(it) }
        )
    )
}
