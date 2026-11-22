package com.raxdenstudios.commons.core.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class ListExtensionTest {

    @Test
    fun `Given a sort of integers, When replaceItem is called, Then the value selected is replaced`() {
        val someItems = listOf(1, 2, 3)

        val result = someItems.replaceItem(4) { itemToReplace -> itemToReplace == 1 }

        assertEquals(listOf(4, 2, 3), result)
    }

    @Test
    fun `Given a sort of integers, When removeItem is called, Then the value selected is removed`() {
        val someItems = listOf(1, 2, 3)

        val result = someItems.removeItem { itemToReplace -> itemToReplace == 1 }

        assertEquals(listOf(2, 3), result)
    }

    @Test
    fun `Given a blank query, When filterByQuery is called, Then return the same list`() {
        val items = listOf("Alpha", "Beta")

        val result = items.filterByQuery("  ") { it }

        assertEquals(items, result)
    }

    @Test
    fun `Given a query with other case, When filterByQuery is called, Then match ignoring case by default`() {
        val result = listOf("Alpha", "Beta", "alphabet").filterByQuery("ALPH") { it }

        assertEquals(listOf("Alpha", "alphabet"), result)
    }

    @Test
    fun `Given ignoreCase false, When filterByQuery is called, Then match case sensitively`() {
        val result = listOf("Alpha", "alphabet").filterByQuery("alph", ignoreCase = false) { it }

        assertEquals(listOf("alphabet"), result)
    }

    @Test
    fun `Given a query matching nothing, When filterByQuery is called, Then return empty list`() {
        val result = listOf("Alpha", "Beta").filterByQuery("zzz") { it }

        assertEquals(emptyList<String>(), result)
    }
}
