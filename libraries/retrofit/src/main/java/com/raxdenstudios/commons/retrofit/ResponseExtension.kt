package com.raxdenstudios.commons.retrofit

import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.commons.ResultData
import retrofit2.Response

@Suppress("MagicNumber", "SwallowedException", "TooGenericExceptionCaught")
inline fun <S : Any, reified R : Any> Response<S>.toResultData(
    errorMessage: String,
    onSuccess: (value: S) -> R = { value -> value as R },
): ResultData<R, NetworkError<S>> = if (!isSuccessful) {
    ResultData.Failure(
        NetworkError.Unknown(
            code = -1,
            body = null,
            message = errorMessage
        )
    )
} else {
    val code = code()
    var body: S? = null

    try {
        body = body()
        when (code) {
            in (200..399) ->
                ResultData.Success(onSuccess(body!!))

            in (400..499) ->
                ResultData.Failure(
                    NetworkError.Client(
                        code = code,
                        body = body,
                        message = errorMessage
                    )
                )

            in (500..599) ->
                ResultData.Failure(
                    NetworkError.Server(
                        code = code,
                        body = body,
                        message = errorMessage
                    )
                )

            else ->
                ResultData.Failure(
                    NetworkError.Unknown(
                        code = code,
                        body = body,
                        message = errorMessage
                    )
                )
        }
    } catch (e: Exception) {
        ResultData.Failure(
            NetworkError.Unknown(
                code = code,
                body = body,
                message = errorMessage
            )
        )
    }
}
