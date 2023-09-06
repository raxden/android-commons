package com.raxdenstudios.commons.retrofit

interface ErrorHandler {

    fun httpError(exception: RetrofitException): Throwable
}
