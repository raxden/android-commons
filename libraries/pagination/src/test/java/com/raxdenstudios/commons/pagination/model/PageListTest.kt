package com.raxdenstudios.commons.pagination.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class PageListTest {

    @Test
    fun `totalItems should return correct size`() {
        val items = listOf("item1", "item2", "item3")
        val pageList = PageList(items, Page(0))

        assertThat(pageList.totalItems).isEqualTo(3)
    }

    @Test
    fun `totalItems should return 0 for empty list`() {
        val pageList = PageList<String>(emptyList(), Page(0))

        assertThat(pageList.totalItems).isEqualTo(0)
    }

    @Test
    fun `map should transform items correctly`() {
        val items = listOf(1, 2, 3)
        val pageList = PageList(items, Page(0))

        val result = pageList.map { it.map { num -> num * 2 } }

        assertThat(result.items).isEqualTo(listOf(2, 4, 6))
        assertThat(result.page).isEqualTo(Page(0))
    }

    @Test
    fun `map should preserve page information`() {
        val items = listOf("a", "b", "c")
        val page = Page(5)
        val pageList = PageList(items, page)

        val result = pageList.map { it.map { str -> str.uppercase() } }

        assertThat(result.items).isEqualTo(listOf("A", "B", "C"))
        assertThat(result.page).isEqualTo(page)
    }

    @Test
    fun `map should handle empty list`() {
        val pageList = PageList<Int>(emptyList(), Page(0))

        val result = pageList.map { it.map { num -> num * 2 } }

        assertThat(result.items).isEmpty()
        assertThat(result.totalItems).isEqualTo(0)
    }

    @Test
    fun `map should change type correctly`() {
        val items = listOf(1, 2, 3)
        val pageList = PageList(items, Page(0))

        val result = pageList.map { it.map { num -> "Number: $num" } }

        assertThat(result.items).isEqualTo(listOf("Number: 1", "Number: 2", "Number: 3"))
    }
}
