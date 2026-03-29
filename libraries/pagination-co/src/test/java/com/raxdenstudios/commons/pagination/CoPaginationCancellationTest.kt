package com.raxdenstudios.commons.pagination

import android.util.Log
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
import kotlinx.coroutines.delay
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
    private val pagination: CoPagination<String> by lazy {
        CoPagination(
            config = Pagination.Config.default,
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
            pageResponse = { pageResult -> pageResponse(pageResult) }
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
    fun `cancelCurrentRequest method exists and can be called`() {
        // Verify that the cancelCurrentRequest method exists and can be called
        pagination.cancelCurrentRequest()
        
        // No exception should be thrown
    }
}
