package com.raxdenstudios.commons.pagination

import android.util.Log
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class CoPaginationCancellationTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val pageRequest: suspend (Page, PageSize) -> PageList<String> = mockk()
    private val pageResponse: (PageResult<String>) -> Unit = mockk(relaxed = true)
    private val responses = mutableListOf<PageResult<String>>()
    private val pagination: CoPagination<String> by lazy {
        CoPagination(
            config = Pagination.Config.default,
            coroutineScope = CoroutineScope(mainDispatcherRule.testDispatcher),
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
    fun `cancelCurrentRequest should cancel ongoing request`() {
        val items = listOf("item1", "item2", "item3")
        var requestStarted = false

        coEvery {
            pageRequest.invoke(any(), any())
        } coAnswers {
            requestStarted = true
            delay(1000) // Simulate long request
            PageList(items, Page(0))
        }

        pagination.requestFirstPage(
            pageRequest = { page, pageSize -> pageRequest(page, pageSize) },
            pageResponse = { pageResult -> pageResponse(pageResult) },
        )

        // Give time for the request to start
        mainDispatcherRule.testDispatcher.scheduler.advanceTimeBy(100)

        pagination.cancelCurrentRequest()

        // Advance time to complete the cancelled request
        mainDispatcherRule.testDispatcher.scheduler.advanceUntilIdle()

        // Verify that Loading was called but Content was not (because it was cancelled)
        coVerify(exactly = 1) { pageResponse.invoke(PageResult.Loading) }
        coVerify(exactly = 0) { pageResponse.invoke(any<PageResult.Content<String>>()) }
    }

    @Test
    fun `Given nothing running, When cancelCurrentRequest is called, Then a later request runs`() {
        val dispatcher = StandardTestDispatcher()
        val pagination = paginationOn(CoroutineScope(dispatcher))

        pagination.cancelCurrentRequest()
        pagination.requestFirstPage(
            pageRequest = { page, _ -> PageList(fullPage(), page) },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(responses).containsExactly(PageResult.Loading, PageResult.Content(fullPage())).inOrder()
    }

    @Test
    fun `Given a request is loading, When it is cancelled, Then a later requestFirstPage runs`() {
        val dispatcher = StandardTestDispatcher()
        val pagination = paginationOn(CoroutineScope(dispatcher))
        pagination.requestFirstPage(pageRequest = { page, _ -> slowPage(page) }, pageResponse = responses::add)
        dispatcher.scheduler.advanceTimeBy(100)
        pagination.cancelCurrentRequest()
        dispatcher.scheduler.advanceUntilIdle()
        responses.clear()

        pagination.requestFirstPage(
            pageRequest = { page, _ -> PageList(fullPage(), page) },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(responses).containsExactly(PageResult.Loading, PageResult.Content(fullPage())).inOrder()
    }

    @Test
    fun `Given a request is loading, When it is cancelled, Then a later requestPage runs`() {
        val dispatcher = StandardTestDispatcher()
        val pagination = paginationOn(CoroutineScope(dispatcher))
        pagination.requestFirstPage(pageRequest = { page, _ -> slowPage(page) }, pageResponse = responses::add)
        dispatcher.scheduler.advanceTimeBy(100)
        pagination.cancelCurrentRequest()
        dispatcher.scheduler.advanceUntilIdle()
        responses.clear()

        pagination.requestPage(
            pageIndex = PageIndex.first,
            pageRequest = { page, _ -> PageList(fullPage(), page) },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(responses).containsExactly(PageResult.Loading, PageResult.Content(fullPage())).inOrder()
    }

    @Test
    fun `Given a replaced job, When it is cancelled late, Then the new job keeps Loading`() {
        val dispatcher = StandardTestDispatcher()
        val pagination = paginationOn(CoroutineScope(dispatcher))
        var invocations = 0
        // The first request ignores cancellation while fetching, so it finishes after the second started.
        pagination.requestFirstPage(
            pageRequest = { page, _ ->
                withContext(NonCancellable) { delay(1_000) }
                PageList(fullPage(), page)
            },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceTimeBy(100)
        pagination.cancelCurrentRequest()
        pagination.requestFirstPage(
            pageRequest = { page, _ -> slowPage(page, delayMillis = 5_000) },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceTimeBy(1_500)

        pagination.requestFirstPage(
            pageRequest = { page, _ ->
                invocations++
                PageList(fullPage(), page)
            },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceTimeBy(1)

        // A probe request is not made: the status is still Loading.
        assertThat(invocations).isEqualTo(0)
    }

    @Test
    fun `Given a scope cancelled before the request starts, When the request is made, Then it never emits Loading`() {
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)
        val pagination = paginationOn(scope)

        pagination.requestFirstPage(pageRequest = { page, _ -> slowPage(page) }, pageResponse = responses::add)
        scope.cancel()
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(responses).doesNotContain(PageResult.Loading)
    }

    @Test
    fun `Given a scope cancelled while loading, When it is cancelled, Then status leaves Loading`() {
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)
        val pagination = paginationOn(scope)
        pagination.requestFirstPage(pageRequest = { page, _ -> slowPage(page) }, pageResponse = responses::add)
        dispatcher.scheduler.advanceTimeBy(100)
        assertThat(readPrivateStatusByReflection(pagination)).isEqualTo("Loading")

        scope.cancel()
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(readPrivateStatusByReflection(pagination)).isEqualTo("Empty")
    }

    @Test
    fun `Given a page loaded, When the same page is fetched again, Then items loaded are not inflated`() {
        val dispatcher = StandardTestDispatcher()
        val pagination = paginationOn(CoroutineScope(dispatcher))
        val requestedPages = mutableListOf<Page>()
        val request: suspend (Page, PageSize) -> PageList<String> = { _, _ -> PageList(fullPage(), Page(0)) }
        pagination.requestFirstPage(request, responses::add)
        dispatcher.scheduler.advanceUntilIdle()
        // The server answers page 0 again: 10 items are loaded, not 20.
        pagination.requestPage(PageIndex(8), request, responses::add)
        dispatcher.scheduler.advanceUntilIdle()

        pagination.requestPage(
            pageIndex = PageIndex(15),
            pageRequest = { page, _ ->
                requestedPages += page
                PageList(fullPage(), page)
            },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(requestedPages).hasSize(1)
    }

    @Test
    fun `Given two pages loaded, When the previous page is requested, Then items loaded are not inflated`() {
        val dispatcher = StandardTestDispatcher()
        val pagination = paginationOn(CoroutineScope(dispatcher))
        val request: suspend (Page, PageSize) -> PageList<String> = { page, _ -> PageList(fullPage(), page) }
        pagination.requestFirstPage(request, responses::add)
        dispatcher.scheduler.advanceUntilIdle()
        pagination.requestPage(PageIndex(8), request, responses::add)
        dispatcher.scheduler.advanceUntilIdle()
        pagination.requestPreviousPage(request, responses::add)
        dispatcher.scheduler.advanceUntilIdle()
        var requested = false

        // 20 items are loaded: index 19 is the end of the list (19 >= 20 - 2), 30 would say it is not.
        pagination.requestPage(
            pageIndex = PageIndex(19),
            pageRequest = { page, _ ->
                requested = true
                PageList(fullPage(), page)
            },
            pageResponse = responses::add,
        )
        dispatcher.scheduler.advanceUntilIdle()

        assertThat(requested).isTrue()
    }

    private fun paginationOn(
        scope: CoroutineScope,
    ) = CoPagination<String>(config = Pagination.Config.default, coroutineScope = scope)

    private suspend fun slowPage(
        page: Page,
        delayMillis: Long = 1_000,
    ): PageList<String> {
        delay(delayMillis)
        return PageList(fullPage(), page)
    }

    private fun fullPage(): List<String> = List(Pagination.Config.default.pageSize.value) { index -> "item$index" }

    // Reflection is deliberate: once the scope is cancelled no further request can reveal the status.
    private fun readPrivateStatusByReflection(
        pagination: CoPagination<String>,
    ): String = Pagination::class.java.getDeclaredField("status")
        .apply { isAccessible = true }
        .get(pagination)
        .let { status -> status::class.java.simpleName }
}
