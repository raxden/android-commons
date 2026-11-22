package com.raxdenstudios.commons.pagination

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import org.junit.Test

internal class PaginationTest {

    private class TestPagination(
        override val config: Config,
    ) : Pagination<String>() {
        override val logger: (message: () -> String) -> Unit = { message -> logs += message() }
        val logs = mutableListOf<String>()

        fun start(
            results: MutableList<PageResult<String>>,
        ) = processRequestStart { results += it }
        fun success(
            page: Int,
            items: List<String>,
            results: MutableList<PageResult<String>>,
        ) = processRequestSuccess(PageList(items, Page(page))) { results += it }
        fun error(
            results: MutableList<PageResult<String>>,
        ) = processRequestError(IllegalStateException("boom")) { results += it }
        fun cancel() = processRequestCancelled()
        fun shouldMake(
            index: Int,
            results: MutableList<PageResult<String>>,
        ) = shouldMakeRequest(PageIndex(index)) { results += it }
        fun page(
            index: PageIndex,
        ): Page = index.toPage()
    }

    private val config = Pagination.Config(Page(0), PageSize(2), prefetchDistance = 1)
    private val pagination = TestPagination(config)
    private val results = mutableListOf<PageResult<String>>()

    @Test
    fun `Given the default config When read Then it has the documented values`() {
        assertThat(Pagination.Config.default.initialPage).isEqualTo(Page(0))
        assertThat(Pagination.Config.default.pageSize).isEqualTo(PageSize.defaultSize)
        assertThat(Pagination.Config.default.prefetchDistance).isEqualTo(2)
    }

    @Test
    fun `Given nothing loaded When getCurrentPage Then returns the initial page`() {
        assertThat(TestPagination(config.copy(initialPage = Page(3))).getCurrentPage()).isEqualTo(Page(3))
    }

    @Test
    fun `Given a loaded page When getCurrentPage and getItemsByPage Then returns the page data`() {
        pagination.success(1, listOf("a", "b"), results)

        assertThat(pagination.getCurrentPage()).isEqualTo(Page(1))
        assertThat(pagination.getItemsByPage(Page(1))).containsExactly("a", "b").inOrder()
        assertThat(pagination.getItemsByPage(Page(9))).isEmpty()
    }

    @Test
    fun `Given loaded data When clear Then the state is reset`() {
        pagination.success(1, listOf("a", "b"), results)

        pagination.clear()

        assertThat(pagination.getCurrentPage()).isEqualTo(Page(0))
        assertThat(pagination.getItemsByPage(Page(1))).isEmpty()
        assertThat(pagination.shouldMake(0, results)).isTrue()
    }

    @Test
    fun `Given a start When processRequestStart Then emits Loading and blocks new requests`() {
        pagination.start(results)

        assertThat(results).containsExactly(PageResult.Loading)
        assertThat(pagination.shouldMake(0, results)).isFalse()
    }

    @Test
    fun `Given a full page When processRequestSuccess Then emits Content and allows more`() {
        pagination.success(0, listOf("a", "b"), results)

        assertThat(results).containsExactly(PageResult.Content(listOf("a", "b")))
        assertThat(pagination.shouldMake(1, results)).isTrue()
        assertThat(pagination.shouldMake(0, results)).isFalse()
    }

    @Test
    fun `Given a short page When processRequestSuccess Then no more results are reported`() {
        pagination.success(0, listOf("a"), results)

        assertThat(pagination.shouldMake(0, results)).isFalse()
        assertThat(results.last()).isEqualTo(PageResult.NoMoreResults)
    }

    @Test
    fun `Given an empty page When processRequestSuccess Then emits NoResults`() {
        pagination.success(0, emptyList(), results)

        assertThat(results).containsExactly(PageResult.NoResults)
    }

    @Test
    fun `Given pages loaded out of order When processRequestSuccess Then content is sorted by page`() {
        pagination.success(1, listOf("c", "d"), results)
        pagination.success(0, listOf("a", "b"), results)

        assertThat(results.last()).isEqualTo(PageResult.Content(listOf("a", "b", "c", "d")))
    }

    @Test
    fun `Given the same page refetched When processRequestSuccess Then items are not counted twice`() {
        pagination.success(0, listOf("a", "b"), results)
        pagination.success(0, listOf("a", "b"), results)

        assertThat(results.last()).isEqualTo(PageResult.Content(listOf("a", "b")))
    }

    @Test
    fun `Given an error with no items When processRequestError Then emits Error and allows retry`() {
        pagination.start(results)

        pagination.error(results)

        assertThat(results.last()).isInstanceOf(PageResult.Error::class.java)
        assertThat(pagination.shouldMake(0, results)).isTrue()
    }

    @Test
    fun `Given an error with items loaded When processRequestError Then state is NotEmpty`() {
        pagination.success(0, listOf("a", "b"), results)
        pagination.start(results)

        pagination.error(results)

        assertThat(results.last()).isInstanceOf(PageResult.Error::class.java)
        assertThat(pagination.shouldMake(0, results)).isFalse()
        assertThat(pagination.shouldMake(1, results)).isTrue()
    }

    @Test
    fun `Given a loading request with no items When cancelled Then new requests are allowed`() {
        pagination.start(results)

        pagination.cancel()

        assertThat(pagination.shouldMake(0, results)).isTrue()
    }

    @Test
    fun `Given a loading request with items When cancelled Then state is NotEmpty`() {
        pagination.success(0, listOf("a", "b"), results)
        pagination.start(results)

        pagination.cancel()

        assertThat(pagination.shouldMake(0, results)).isFalse()
        assertThat(pagination.shouldMake(1, results)).isTrue()
    }

    @Test
    fun `Given no loading request When cancelled Then the state is untouched`() {
        pagination.success(0, listOf("a"), results)

        pagination.cancel()

        results.clear()
        assertThat(pagination.shouldMake(0, results)).isFalse()
        assertThat(results).containsExactly(PageResult.NoMoreResults)
    }

    @Test
    fun `Given a NotEmpty state When shouldMakeRequest Then logs the end of list calculation`() {
        pagination.success(0, listOf("a", "b"), results)

        pagination.shouldMake(1, results)

        assertThat(pagination.logs).containsExactly("true <- 1 >= (2 - 1)")
    }

    @Test
    fun `Given the first index When toPage Then returns the initial page`() {
        val custom = TestPagination(config.copy(initialPage = Page(1)))

        assertThat(custom.page(PageIndex.first)).isEqualTo(Page(1))
    }

    @Test
    fun `Given another index When toPage Then adds prefetch distance and initial page`() {
        val custom = TestPagination(Pagination.Config(Page(1), PageSize(10), prefetchDistance = 2))

        assertThat(custom.page(PageIndex(5))).isEqualTo(Page(1))
        assertThat(custom.page(PageIndex(20))).isEqualTo(Page(3))
    }
}
