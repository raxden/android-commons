package com.raxdenstudios.commons.retrofit

import com.raxdenstudios.commons.ResultData
import retrofit2.Response

@Suppress("TooGenericExceptionCaught", "ReturnCount")
fun <T : Any> Response<T>.toResultData(
    exceptionMessage: String
): ResultData<T, Exception> {
    if (isSuccessful) {
        try {
            val body = body()
            if (body != null)
                return ResultData.Success(body)
        } catch (e: Exception) {
            return ResultData.Failure(e)
        }
    }
    return ResultData.Failure(IllegalStateException(exceptionMessage))
}

@Suppress("TooGenericExceptionCaught", "ReturnCount")
fun <T : Any, R : Any> Response<T>.toResultData(
    exceptionMessage: String,
    map: (T) -> R
): ResultData<R, Exception> {
    if (isSuccessful) {
        try {
            val body = body()
            if (body != null)
                return ResultData.Success(map(body))
        } catch (e: Exception) {
            return ResultData.Failure(e)
        }
    }
    return ResultData.Failure(IllegalStateException(exceptionMessage))
}
