package com.raxdenstudios.commons.pagination.model

data class PageSize(
    val value: Int,
) {

    init {
        require(value > 0) { "PageSize must be greater than 0, was $value" }
    }

    companion object {
        val defaultSize = PageSize(10)
    }
}
