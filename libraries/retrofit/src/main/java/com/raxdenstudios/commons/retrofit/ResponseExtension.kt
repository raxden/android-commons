package com.raxdenstudios.commons.retrofit

import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.commons.ResultData
import retrofit2.Response

@Suppress("MagicNumber", "SwallowedException", "TooGenericExceptionCaught")
fun <S : Any> Response<S>.toResultData(
    errorMessage: String,
): ResultData<S, NetworkError<S>> {

    val code = code()
    var body: S? = null

    return try {
        body = body()
        when (code) {
            in (200..399) ->
                ResultData.Success(body!!)

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
    } catch (e: Throwable) {
        ResultData.Failure(
            NetworkError.Unknown(
                code = code,
                body = body,
                message = errorMessage
            )
        )
    }
}
