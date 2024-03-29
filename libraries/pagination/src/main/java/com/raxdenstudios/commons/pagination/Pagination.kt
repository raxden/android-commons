package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize

@Suppress("TooManyFunctions")
abstract class Pagination<T> {

    abstract val config: Config
    abstract val logger: (message: String) -> Unit

    private val history: MutableMap<Page, List<T>> = mutableMapOf()
    private var itemsLoaded: Int = 0
    private var status: Status = Status.Empty
    private var currentPage: Page? = null

    fun getCurrentPage(): Page {
        return currentPage ?: config.initialPage
    }

    fun getItemsByPage(page: Page) = history[page] ?: emptyList()

    fun clear() {
        history.clear()
        currentPage = null
        itemsLoaded = 0
        status = Status.Empty
    }

    protected fun processRequestError(
        throwable: Throwable,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        status = if (itemsLoadedGreaterThan0()) Status.NotEmpty
        else Status.Empty

        pageResponse(PageResult.Error(throwable))
    }

    private fun itemsLoadedGreaterThan0() = itemsLoaded > 0

    private fun MutableMap<Page, List<T>>.getAllItems() =
        mutableListOf<T>().also { list ->
            toSortedMap(compareBy { page -> page.value }).values
                .forEach { list.addAll(it) }
        }.toList()

    private fun thereAreLessElementsThanPageSize(pageList: PageList<T>) =
        pageList.totalItems < config.pageSize.value

    protected fun processRequestStart(pageResponse: (pageResult: PageResult<T>) -> Unit) {
        status = Status.Loading
        pageResponse(PageResult.Loading)
    }

    protected fun PageIndex.toPage(): Page {
        return if (this == PageIndex.first) config.initialPage
        else {
            val indexWithPrefetchDistance = value + 1 + config.prefetchDistance
            val pageSize = config.pageSize.value
            Page(indexWithPrefetchDistance / pageSize + config.initialPage.value)
        }
    }

    protected fun processRequestSuccess(
        pageList: PageList<T>,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) {
        itemsLoaded += pageList.totalItems
        status = if (thereAreLessElementsThanPageSize(pageList)) Status.NoMoreResults
        else Status.NotEmpty
        currentPage = pageList.page
        history[pageList.page] = pageList.items

        val items = history.getAllItems()

        val pageResult = if (items.isEmpty()) PageResult.NoResults
        else PageResult.Content(items)

        pageResponse(pageResult)
    }

    protected fun shouldMakeRequest(
        pageIndex: PageIndex,
        pageResponse: (pageResult: PageResult<T>) -> Unit,
    ) = when (status) {
        Status.Empty -> true
        Status.NotEmpty -> indexItsEndOfTheList(pageIndex, itemsLoaded)
        Status.Loading -> false
        Status.NoMoreResults -> {
            pageResponse(PageResult.NoMoreResults)
            false
        }
    }

    private fun indexItsEndOfTheList(pageIndex: PageIndex, itemsLoaded: Int): Boolean {
        val endOfTheList = pageIndex.value >= (itemsLoaded - config.prefetchDistance)
        logger("$endOfTheList <- ${pageIndex.value} >= ($itemsLoaded - ${config.prefetchDistance})")
        return endOfTheList
    }

    protected sealed class Status {
        object Empty : Status()
        object NotEmpty : Status()
        object Loading : Status()
        object NoMoreResults : Status()
    }

    data class Config(
        val initialPage: Page,
        val pageSize: PageSize,
        val prefetchDistance: Int,
    ) {

        companion object {

            val default = Config(
                initialPage = Page(0),
                pageSize = PageSize.defaultSize,
                prefetchDistance = 2
            )
        }
    }
}
