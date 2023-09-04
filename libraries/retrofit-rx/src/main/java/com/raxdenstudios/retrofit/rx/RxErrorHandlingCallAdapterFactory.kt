package com.raxdenstudios.retrofit.rx

import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor(
    private val callAdapter: RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create(),
    private val errorHandler: ErrorHandler
) : CallAdapter.Factory() {

    companion object {
        fun create(errorHandler: ErrorHandler) = RxErrorHandlingCallAdapterFactory(
            errorHandler = errorHandler
        )
    }

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *> =
        RxCallAdapterWrapper(
            retrofit,
            callAdapter.get(returnType, annotations, retrofit)!!,
            errorHandler
        )
}
