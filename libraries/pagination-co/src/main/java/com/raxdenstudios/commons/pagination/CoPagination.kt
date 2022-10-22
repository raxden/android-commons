@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.ext.launch
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.CoroutineScope

class CoPagination<T>(
    override val config: Config = Config.default,
    override val logger: (message: String) -> Unit = {},
    private val coroutineScope: CoroutineScope,
) : Pagination<T>() {

    fun requestFirstPage(
        pageRequest: suspend (page: Page, pageSize: PageSize) -> PageList<T>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        requestPage(PageIndex.first, pageRequest, pageResponse)
    }

    fun requestPreviousPage(
        pageRequest: suspend (page: Page, pageSize: PageSize) -> PageList<T>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        if (getCurrentPage() == config.initialPage) return
        val previousPage = Page(getCurrentPage().value - 1)
        makeRequest(previousPage, pageRequest, pageResponse)
    }

    fun requestPage(
        pageIndex: PageIndex,
        pageRequest: suspend (page: Page, pageSize: PageSize) -> PageList<T>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        if (shouldMakeRequest(pageIndex, pageResponse)) {
            val page = pageIndex.toPage()
            makeRequest(page, pageRequest, pageResponse)
        }
    }

    private fun makeRequest(
        page: Page,
        pageRequest: suspend (page: Page, pageSize: PageSize) -> PageList<T>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        coroutineScope.launch(
            onError = { error -> processRequestError(error, pageResponse) }
        ) {
            processRequestStart(pageResponse)
            processRequestSuccess(
                pageList = pageRequest.invoke(page, config.pageSize),
                pageResponse = pageResponse
            )
        }
    }
}
