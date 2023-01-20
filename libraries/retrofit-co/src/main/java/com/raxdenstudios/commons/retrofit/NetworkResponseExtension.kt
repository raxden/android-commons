package com.raxdenstudios.commons.retrofit

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.commons.ResultData

@Suppress("MagicNumber")
inline fun <T : Any, U : Any, reified R : Any> NetworkResponse<T, U>.toResultData(
    errorMessage: String,
    transform: (value: T) -> R = { value -> value as R }
): com.raxdenstudios.commons.ResultData<R> = when (this) {
    is NetworkResponse.Success -> com.raxdenstudios.commons.ResultData.Success(transform(body))
    is NetworkResponse.ServerError -> {
        when (val code = code ?: -1) {
            in (400..499) -> com.raxdenstudios.commons.ResultData.Error(NetworkException.Client(code, errorMessage))
            in (500..599) -> com.raxdenstudios.commons.ResultData.Error(NetworkException.Server(code, errorMessage))
            else -> com.raxdenstudios.commons.ResultData.Error(NetworkException.Unknown(errorMessage))
        }
    }
    is NetworkResponse.NetworkError -> com.raxdenstudios.commons.ResultData.Error(
        NetworkException.Network(
            errorMessage,
            error
        )
    )
    is NetworkResponse.UnknownError -> com.raxdenstudios.commons.ResultData.Error(
        NetworkException.Unknown(
            errorMessage,
            error
        )
    )
}
