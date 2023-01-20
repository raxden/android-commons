package com.raxdenstudios.commons.retrofit

import com.raxdenstudios.commons.ResultData
import retrofit2.Response
import java.io.IOException

@Suppress("TooGenericExceptionCaught", "ReturnCount")
fun <T : Any> Response<T>.toResultData(
    exceptionMessage: String
): com.raxdenstudios.commons.ResultData<T> {
    if (isSuccessful) {
        try {
            val body = body()
            if (body != null)
                return com.raxdenstudios.commons.ResultData.Success(body)
        } catch (e: Exception) {
            return com.raxdenstudios.commons.ResultData.Error(IOException(exceptionMessage))
        }
    }
    return com.raxdenstudios.commons.ResultData.Error(IOException(exceptionMessage))
}

@Suppress("TooGenericExceptionCaught", "ReturnCount")
fun <T : Any, R : Any> Response<T>.toResultData(
    exceptionMessage: String,
    map: (T) -> R
): com.raxdenstudios.commons.ResultData<R> {
    if (isSuccessful) {
        try {
            val body = body()
            if (body != null)
                return com.raxdenstudios.commons.ResultData.Success(map(body))
        } catch (e: Exception) {
            return com.raxdenstudios.commons.ResultData.Error(IOException(exceptionMessage, e))
        }
    }
    return com.raxdenstudios.commons.ResultData.Error(IOException(exceptionMessage))
}
