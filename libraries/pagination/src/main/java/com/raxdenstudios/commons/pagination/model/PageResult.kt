package com.raxdenstudios.commons.pagination.model

sealed class PageResult<out T> {
    data object Loading : PageResult<Nothing>()
    data class Content<T>(val items: List<T>) : PageResult<T>()
    data class Error(val throwable: Throwable) : PageResult<Nothing>()
    data object NoResults : PageResult<Nothing>()
    data object NoMoreResults : PageResult<Nothing>()
}
