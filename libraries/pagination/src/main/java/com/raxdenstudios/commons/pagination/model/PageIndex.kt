package com.raxdenstudios.commons.pagination.model

data class PageIndex(val value: Int) {

    companion object {

        val first = PageIndex(0)
    }
}
