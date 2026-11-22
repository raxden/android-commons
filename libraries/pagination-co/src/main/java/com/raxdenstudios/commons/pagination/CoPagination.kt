@file:Suppress("TooManyFunctions")

package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.coroutines.ext.launch
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive

class CoPagination<T>(
    override val config: Config = Config.default,
    override val logger: (message: () -> String) -> Unit = {},
    private val coroutineScope: CoroutineScope,
) : Pagination<T>() {

    @Volatile
    private var currentJob: Job? = null

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
        currentJob?.cancel()
        val job = coroutineScope.launch(
            onError = { error -> processRequestError(error, pageResponse) },
        ) {
            processRequestStart(pageResponse)
            val pageList = pageRequest.invoke(page, config.pageSize)
            // A request cancelled while fetching must not overwrite the state of the one that replaced it.
            ensureActive()
            processRequestSuccess(
                pageList = pageList,
                pageResponse = pageResponse,
            )
        }
        currentJob = job
        job.invokeOnCompletion { cause ->
            // Only the current request owns the status: a replaced one must not reset the new one's loading.
            if (cause is CancellationException && currentJob === job) processRequestCancelled()
        }
    }

    fun cancelCurrentRequest() {
        val job = currentJob ?: return
        currentJob = null
        job.cancel()
        processRequestCancelled()
    }
}
