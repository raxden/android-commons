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
}
