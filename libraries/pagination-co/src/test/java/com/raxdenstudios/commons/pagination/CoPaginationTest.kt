package com.raxdenstudios.commons.pagination

import android.util.Log
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class CoPaginationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pageRequest: (Page, PageSize) -> PageList<String> = mockk()
    private val pageResponse: (PageResult<String>) -> PageResult<String> = mockk(relaxed = true)
    private val pagination: CoPagination<String> by lazy {
        CoPagination(
            config = paginationConfig,
            coroutineScope = CoroutineScope(mainDispatcherRule.testDispatcher)
        )
    }

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
    }

    @After
    fun after() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `requestFirstPage should return the right states`() {
        givenAPageWithResults(aFirstPage)

        pagination.requestFirstPage(
            pageRequest = { page, pageSize -> pageRequest(page, pageSize) },
            pageResponse = { pageResult -> pageResponse(pageResult) }
        )

        coVerifyOrder {
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(PageResult.Content(aPageItems))
        }
    }

    @Test
    fun `requestPage should return the right states`() {
        givenAPageWithResults(aFirstPage)

        requestPage(PageIndex.first)

        coVerifyOrder {
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(PageResult.Content(aPageItems))
        }
        confirmVerified(pageResponse)
    }

    @Test
    fun `Given a first page with incomplete results (less than 10), When requestPage is called several times, Then return the right states`() {
        givenAPageWithIncompleteResults(aFirstPage)

        for (index in PageIndex.first.value..4) requestPage(PageIndex(index))

        coVerifyOrder {
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(PageResult.Content(aIncompletePageItems))
            pageResponse.invoke(PageResult.NoMoreResults)
            pageResponse.invoke(PageResult.NoMoreResults)
            pageResponse.invoke(PageResult.NoMoreResults)
            pageResponse.invoke(PageResult.NoMoreResults)
        }
        confirmVerified(pageResponse)
    }

    @Test
    fun `Given a first page, When requestPage is called several times, Then return the right states`() {
        givenAPageWithResults(aFirstPage)
        givenAPageWithResults(Page(1))
        givenAPageWithResults(Page(2))
        givenAPageWithResults(Page(3))
        givenAPageWithResults(Page(4))
        givenAPageWithResults(Page(5))

        for (index in PageIndex.first.value..48) requestPage(PageIndex(index))

        coVerifyOrder {
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(
                PageResult.Content(
                    aPageItems
                )
            )
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(
                PageResult.Content(
                    aPageItems + aPageItems
                )
            )
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(
                PageResult.Content(
                    aPageItems + aPageItems + aPageItems
                )
            )
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(
                PageResult.Content(
                    aPageItems + aPageItems + aPageItems + aPageItems
                )
            )
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(
                PageResult.Content(
                    aPageItems + aPageItems + aPageItems + aPageItems + aPageItems
                )
            )
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(
                PageResult.Content(
                    aPageItems + aPageItems + aPageItems + aPageItems + aPageItems + aPageItems
                )
            )
        }
        confirmVerified(pageResponse)
    }

    @Test
    fun `Given an error that happens when a page is requested, When requestPage is called, Then return the right states`() {
        givenAPageWithResults(aFirstPage)
        givenAPageWithError(Page(1))

        for (index in PageIndex.first.value..8) requestPage(PageIndex(index))

        val pageResultSlot = slot<PageResult<String>>()
        coVerifyOrder {
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(PageResult.Content(aPageItems))
            pageResponse.invoke(PageResult.Loading)
            pageResponse.invoke(capture(pageResultSlot))
        }
        assertTrue(pageResultSlot.captured is PageResult.Error)
        confirmVerified(pageResponse)
    }

    private fun requestPage(pageIndex: PageIndex) {
        pagination.requestPage(
            pageIndex = pageIndex,
            pageRequest = { page, pageSize -> pageRequest(page, pageSize) },
            pageResponse = { pageResult -> pageResponse(pageResult) }
        )
    }

    private fun givenAPageWithError(page: Page) {
        coEvery {
            pageRequest.invoke(page, aPageSize)
        } coAnswers { error("IllegalStateException") }
    }

    private fun givenAPageWithResults(page: Page) {
        coEvery {
            pageRequest.invoke(page, aPageSize)
        } returns PageList(aPageItems, page)
    }

    private fun givenAPageWithIncompleteResults(page: Page) {
        coEvery {
            pageRequest.invoke(page, aPageSize)
        } returns PageList(aIncompletePageItems, page)
    }

    companion object {

        private const val PREFETCH_DISTANCE = 2
        private val aFirstPage = Page(0)
        private val aPageSize = PageSize.defaultSize
        private val aIncompletePageItems = listOf(
            "item_1",
            "item_2",
            "item_3",
            "item_4"
        )
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
        private val paginationConfig = Pagination.Config(
            initialPage = aFirstPage,
            pageSize = aPageSize,
            prefetchDistance = PREFETCH_DISTANCE
        )
    }
}
