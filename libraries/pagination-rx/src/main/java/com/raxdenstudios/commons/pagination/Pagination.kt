package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.ext.subscribeWith
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

class Pagination<T>(
  private val config: Config = Config.default,
  private val compositeDisposable: CompositeDisposable
) {

  private val history: MutableMap<Page, List<T>> = mutableMapOf()
  private var currentPage: Page = Page.firstPage
  private var itemsLoaded: Int = 0
  private var status: Status = Status.Empty

  fun requestPage(
    pageIndex: PageIndex = PageIndex.firstPage,
    pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    if (shouldMakeRequest(pageIndex, pageResponse)) {
      val page = pageIndex.toPage()
      makeRequest(page, pageRequest, pageResponse)
    }
  }

  fun requestPreviousPage(
    pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    if (currentPage == Page.firstPage) return
    val previousPage = Page(currentPage.value - 1)
    makeRequest(previousPage, pageRequest, pageResponse)
  }

  fun getItemsByPage(page: Page) = history[page] ?: emptyList()

  fun clear() {
    history.clear()
    currentPage = Page.firstPage
    itemsLoaded = 0
    status = Status.Empty
  }

  private fun makeRequest(
    page: Page,
    pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    val request = pageRequest(page, config.pageSize)
    processRequest(request, pageResponse)
  }

  private fun processRequest(
    request: Single<PageList<T>>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    request
      .map { pageList -> processRequest(pageList) }
      .subscribeWith(
        onStart = { processRequestStart(pageResponse) },
        onSuccess = { pageResult -> processRequestSuccess(pageResult, pageResponse) },
        onError = { throwable -> processRequestError(throwable, pageResponse) }
      )
      .addTo(compositeDisposable)
  }

  private fun processRequest(
    pageList: PageList<T>
  ): PageResult<T> {
    itemsLoaded += pageList.totalItems
    status = if (thereAreLessElementsThanPageSize(pageList)) Status.NoMoreResults
    else Status.NotEmpty
    currentPage = pageList.page
    history[pageList.page] = pageList.items

    val items = history.getAllItems()
    return if (items.isEmpty()) PageResult.NoResults
    else PageResult.Content(items)
  }

  private fun processRequestError(
    throwable: Throwable,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    status = if (itemsLoadedGreaterThan0()) Status.NotEmpty
    else Status.Empty

    pageResponse(PageResult.Error(throwable))
  }

  private fun itemsLoadedGreaterThan0() = itemsLoaded > 0

  private fun processRequestSuccess(
    pageResult: PageResult<T>,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    pageResponse(pageResult)
  }

  private fun MutableMap<Page, List<T>>.getAllItems() = mutableListOf<T>().also { list ->
    toSortedMap(compareBy { page -> page.value }).values.forEach { list.addAll(it) }
  }.toList()

  private fun thereAreLessElementsThanPageSize(pageList: PageList<T>) =
    pageList.totalItems < config.pageSize.value

  private fun processRequestStart(
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) {
    status = Status.Loading

    pageResponse(PageResult.Loading)
  }

  private fun PageIndex.toPage(): Page {
    return if (this == PageIndex.firstPage) config.initialPage
    else Page((this.value + config.prefetchDistance) / config.pageSize.value)
  }

  private fun shouldMakeRequest(
    pageIndex: PageIndex,
    pageResponse: (pageResult: PageResult<T>) -> Unit
  ) = when (status) {
    Status.Empty -> true
    Status.NotEmpty -> indexItsEndOfTheList(pageIndex, itemsLoaded)
    Status.Loading -> false
    Status.NoMoreResults -> {
      pageResponse(PageResult.NoMoreResults)
      false
    }
  }

  private fun indexItsEndOfTheList(pageIndex: PageIndex, itemsLoaded: Int) =
    pageIndex.value >= (itemsLoaded - config.prefetchDistance)

  private sealed class Status {
    object Empty : Status()
    object NotEmpty : Status()
    object Loading : Status()
    object NoMoreResults : Status()
  }

  class Config(
    val initialPage: Page,
    val pageSize: PageSize,
    val prefetchDistance: Int
  ) {

    companion object {
      val default = Config(
        initialPage = Page.firstPage,
        pageSize = PageSize.defaultSize,
        prefetchDistance = 2
      )
    }
  }
}
