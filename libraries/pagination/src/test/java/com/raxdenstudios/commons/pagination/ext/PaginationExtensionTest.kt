package com.raxdenstudios.commons.pagination.ext

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridItemInfo
import androidx.compose.foundation.lazy.grid.LazyGridLayoutInfo
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.pagination.model.PageIndex
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class PaginationExtensionTest {

    @Test
    fun `LinearLayoutManager toPageIndex should return correct last visible position`() {
        val layoutManager = mockk<LinearLayoutManager>()
        every { layoutManager.findFirstVisibleItemPosition() } returns 5
        every { layoutManager.childCount } returns 10

        val result = layoutManager.toPageIndex()

        assertThat(result.value).isEqualTo(14) // 5 + 10 - 1
    }

    @Test
    fun `LinearLayoutManager toPageIndex should return 0 when no items visible`() {
        val layoutManager = mockk<LinearLayoutManager>()
        every { layoutManager.findFirstVisibleItemPosition() } returns -1
        every { layoutManager.childCount } returns 0

        val result = layoutManager.toPageIndex()

        assertThat(result.value).isEqualTo(0) // coerceAtLeast(0)
    }

    @Test
    fun `LinearLayoutManager toPageIndex should handle first item visible`() {
        val layoutManager = mockk<LinearLayoutManager>()
        every { layoutManager.findFirstVisibleItemPosition() } returns 0
        every { layoutManager.childCount } returns 5

        val result = layoutManager.toPageIndex()

        assertThat(result.value).isEqualTo(4) // 0 + 5 - 1
    }

    @Test
    fun `LazyGridState toPageIndex should return correct last visible index`() {
        val gridState = mockk<LazyGridState>()
        val layoutInfo = mockk<LazyGridLayoutInfo>()
        val visibleItems = List(8) { mockk<LazyGridItemInfo>() }

        every { gridState.firstVisibleItemIndex } returns 10
        every { gridState.layoutInfo } returns layoutInfo
        every { layoutInfo.visibleItemsInfo } returns visibleItems

        val result = gridState.toPageIndex()

        assertThat(result.value).isEqualTo(17) // 10 + 8 - 1
    }

    @Test
    fun `LazyGridState toPageIndex should return 0 when no items visible`() {
        val gridState = mockk<LazyGridState>()
        val layoutInfo = mockk<LazyGridLayoutInfo>()

        every { gridState.firstVisibleItemIndex } returns 0
        every { gridState.layoutInfo } returns layoutInfo
        every { layoutInfo.visibleItemsInfo } returns emptyList()

        val result = gridState.toPageIndex()

        assertThat(result.value).isEqualTo(0) // 0 + 0 - 1 = -1, coerceAtLeast(0)
    }

    @Test
    fun `LazyListState toPageIndex should return correct last visible index`() {
        val listState = mockk<LazyListState>()
        val layoutInfo = mockk<androidx.compose.foundation.lazy.LazyListLayoutInfo>()
        val visibleItems = List(12) { mockk<androidx.compose.foundation.lazy.LazyListItemInfo>() }

        every { listState.firstVisibleItemIndex } returns 20
        every { listState.layoutInfo } returns layoutInfo
        every { layoutInfo.visibleItemsInfo } returns visibleItems

        val result = listState.toPageIndex()

        assertThat(result.value).isEqualTo(31) // 20 + 12 - 1
    }

    @Test
    fun `LazyListState toPageIndex should return 0 when no items visible`() {
        val listState = mockk<LazyListState>()
        val layoutInfo = mockk<androidx.compose.foundation.lazy.LazyListLayoutInfo>()

        every { listState.firstVisibleItemIndex } returns 0
        every { listState.layoutInfo } returns layoutInfo
        every { layoutInfo.visibleItemsInfo } returns emptyList()

        val result = listState.toPageIndex()

        assertThat(result.value).isEqualTo(0) // 0 + 0 - 1 = -1, coerceAtLeast(0)
    }

    @Test
    fun `LazyListState toPageIndex should handle single item visible`() {
        val listState = mockk<LazyListState>()
        val layoutInfo = mockk<androidx.compose.foundation.lazy.LazyListLayoutInfo>()
        val visibleItems = List(1) { mockk<androidx.compose.foundation.lazy.LazyListItemInfo>() }

        every { listState.firstVisibleItemIndex } returns 5
        every { listState.layoutInfo } returns layoutInfo
        every { layoutInfo.visibleItemsInfo } returns visibleItems

        val result = listState.toPageIndex()

        assertThat(result.value).isEqualTo(5) // 5 + 1 - 1
    }
}
