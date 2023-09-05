package com.raxdenstudios.retrofit.rx

interface ErrorHandler {

    fun httpError(exception: RetrofitException): Throwable
}
