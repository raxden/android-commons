package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData

@Suppress("MagicNumber")
inline fun <T : Any, U : Any, reified R : Any> NetworkResponse<T, U>.toResultData(
    errorMessage: String,
    transform: (value: T) -> R = { value -> value as R }
): ResultData<R, NetworkError> = when (this) {
    is NetworkResponse.Success -> ResultData.Success(transform(body))
    is NetworkResponse.ServerError -> {
        when (val code = code ?: -1) {
            in (400..499) -> ResultData.Failure(NetworkError.Client(code, errorMessage))
            in (500..599) -> ResultData.Failure(NetworkError.Server(code, errorMessage))
            else -> ResultData.Failure(NetworkError.Unknown(errorMessage))
        }
    }
    is NetworkResponse.NetworkError -> ResultData.Failure(NetworkError.Network(errorMessage))
    is NetworkResponse.UnknownError -> ResultData.Failure(NetworkError.Unknown(errorMessage))
}
