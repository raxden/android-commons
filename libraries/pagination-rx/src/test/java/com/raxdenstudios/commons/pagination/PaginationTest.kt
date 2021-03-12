package com.raxdenstudios.commons.pagination

import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.test.rules.RxSchedulerRule
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyOrder
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.junit.Rule
import org.junit.Test

internal class PaginationTest {

  @get:Rule
  val rxSchedulerRule = RxSchedulerRule()

  private val configuration = Pagination.Config.default
  private val compositeDisposable: CompositeDisposable = mockk(relaxed = true)
  private var pageRequest: (page: Page, pageSize: PageSize) -> Single<PageList<String>> = mockk()
  private var pageResponse: (result: PageResult<String>) -> PageResult<String> =
    mockk(relaxed = true)

  private val pagination: Pagination<String> by lazy {
    Pagination(configuration, compositeDisposable)
  }

  @Test
  fun `When requestPage is called, Then a Loading and Content states are returned`() {
    givenAPageWithResults(Page.firstPage)

    requestPage(PageIndex.firstPage)

    verifyOrder {
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems))
    }
    confirmVerified(pageResponse)
  }

  @Test
  fun `Given a first page with incomplete results (less than 10), When requestPage is called several times, Then loading, content and noMoreResults states are returned`() {
    givenAPageWithIncompleteResults(Page.firstPage)

    for (index in PageIndex.firstPage.value..10) requestPage(PageIndex(index))

    verifyOrder {
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aIncompletePageItems))
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
      pageResponse.invoke(PageResult.NoMoreResults)
    }
    confirmVerified(pageResponse)
  }

  @Test
  fun `Given a first page, When requestPage is called several times, Then loading, content states are returned for every page`() {
    givenAPageWithResults(Page.firstPage)
    givenAPageWithResults(Page(1))
    givenAPageWithResults(Page(2))
    givenAPageWithResults(Page(3))
    givenAPageWithResults(Page(4))
    givenAPageWithResults(Page(5))

    for (index in PageIndex.firstPage.value..48) requestPage(PageIndex(index))

    verifyOrder {
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems + aPageItems + aPageItems))
      pageResponse.invoke(PageResult.Loading)
      pageResponse.invoke(PageResult.Content(aPageItems + aPageItems + aPageItems + aPageItems + aPageItems + aPageItems))
    }
    confirmVerified(pageResponse)
  }

  private fun requestPage(pageIndex: PageIndex) {
    pagination.requestPage(
      pageIndex = pageIndex,
      pageRequest = { page, pageSize -> pageRequest(page, pageSize) },
      pageResponse = { pageResult -> pageResponse(pageResult) }
    )
  }

  private fun givenAPageWithResults(page: Page) {
    every { pageRequest.invoke(page, PageSize.defaultSize) } returns Single.just(
      PageList(aPageItems, page)
    )
  }

  private fun givenAPageWithIncompleteResults(page: Page) {
    every { pageRequest.invoke(page, PageSize.defaultSize) } returns Single.just(
      PageList(aIncompletePageItems, page)
    )
  }
}

private val aIncompletePageItems = listOf("item_1", "item_2", "item_3", "item_4")
private val aPageItems = listOf(
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item",
  "item"
)
