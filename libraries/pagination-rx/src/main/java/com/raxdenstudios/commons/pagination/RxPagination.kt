@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.ext.subscribeWith
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class RxPagination<T>(
    override val config: Config = Config.default,
    override val logger: (message: String) -> Unit = {},
    private val compositeDisposable: CompositeDisposable,
) : Pagination<T>() {

    fun requestFirstPage(
        pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        requestPage(PageIndex.first, pageRequest, pageResponse)
    }

    fun requestPreviousPage(
        pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        if (getCurrentPage() == config.initialPage) return
        val previousPage = Page(getCurrentPage().value - 1)
        makeRequest(previousPage, pageRequest, pageResponse)
    }

    fun requestPage(
        pageIndex: PageIndex,
        pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        if (shouldMakeRequest(pageIndex, pageResponse)) {
            val page = pageIndex.toPage()
            makeRequest(page, pageRequest, pageResponse)
        }
    }

    private fun makeRequest(
        page: Page,
        pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        val request = pageRequest(page, config.pageSize)
        processRequest(request, pageResponse)
    }

    private fun processRequest(
        request: Single<PageList<T>>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        request
            .subscribeWith(
                onStart = { processRequestStart(pageResponse) },
                onSuccess = { pageList -> processRequestSuccess(pageList, pageResponse) },
                onError = { throwable -> processRequestError(throwable, pageResponse) }
            )
            .addTo(compositeDisposable)
    }
}
