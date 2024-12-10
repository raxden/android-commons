package com.raxdenstudios.commons.core.ext

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

internal class AnyExtensionTest {

    @Test
    fun `castAs should return right value`() {
        val value = "String".castAs<String>()

        assertEquals("String", value)
    }

    @Test
    fun `castAs should return null value`() {
        val value = Boolean.castAs<String>()

        assertNull(value)
    }

    @Test
    fun `requireAs should return right value`() {
        val value = "String".requireAs<String>()

        assertEquals("String", value)
    }

    @Test
    fun `letAs should return right value`() {
        val value = "String".letAs<String, Boolean> { it == "String" }

        assertNotNull(value)
        assertTrue(value ?: false)
    }

    @Test
    fun `runAs should return right value`() {
        val value = "String".runAs<String, Boolean> { true }

        assertNotNull(value)
        assertTrue(value ?: false)
    }

    @Test
    fun `applyAs should return right value`() {
        val value = "String".applyAs<String> { length }

        assertEquals("String", value)
    }

    @Test
    fun `alsoAs should return right value`() {
        val value = "String".alsoAs<String> { it.length }

        assertEquals("String", value)
    }

    @Test
    fun `applyWhen should return right value`() {
        val value = "String".applyWhen(true) { length }

        assertEquals("String", value)
    }

    @Test
    fun `alsoWhen should return right value`() {
        val value = "String".alsoWhen(true) { it.length }

        assertEquals("String", value)
    }
}
