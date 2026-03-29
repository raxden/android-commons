package com.raxdenstudios.commons.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ErrorResponseInterceptor : Interceptor {

    @Suppress("MagicNumber")
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())

        if (!response.isSuccessful) {
            val errorBody = response.body?.string()
            throw when (response.code) {
                401 -> UnauthorizedException(errorBody)
                403 -> ForbiddenException(errorBody)
                404 -> NotFoundException(errorBody)
                500 -> ServerException(errorBody)
                else -> HttpException(response.code, errorBody)
            }
        }

        return response
    }
}

open class HttpException(
    val code: Int,
    val errorBody: String?
) : IOException("HTTP $code: ${errorBody ?: "Unknown error"}")

class UnauthorizedException(errorBody: String?) : HttpException(401, errorBody)
class ForbiddenException(errorBody: String?) : HttpException(403, errorBody)
class NotFoundException(errorBody: String?) : HttpException(404, errorBody)
class ServerException(errorBody: String?) : HttpException(500, errorBody)
