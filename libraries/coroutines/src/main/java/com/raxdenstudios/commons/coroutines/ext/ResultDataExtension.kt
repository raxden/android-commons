@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.coroutines.ext

import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.coThen
import com.raxdenstudios.commons.core.ext.coThenFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

fun <T, R, E> Flow<ResultData<T, E>>.then(
    function: suspend (value: T) -> R
): Flow<ResultData<R, E>> =
    map { result -> result.coThen { function(it) } }

fun <T, R, E> Flow<ResultData<T, E>>.thenFailure(
    function: (value: E) -> R
): Flow<ResultData<T, R>> =
    map { result -> result.coThenFailure { function(it) } }
