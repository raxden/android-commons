package com.raxdenstudios.commons.core.ext

import org.junit.Assert.assertEquals
import org.junit.Test

internal class EncryptExtensionTest {

    @Test
    fun `toSHA256 should return value`() {
        val value = "Generando un SHA256 de un texto"

        assertEquals(
            "b2303cd72f71b642f9c57d2b053ecd6521608b4b2f3045cae612090c472c9eff",
            value.toSHA256(),
        )
    }

    @Test
    fun `toSHA256 with UTF_16 should return value`() {
        val value = "Generando un SHA256 de un texto"

        assertEquals(
            "018e20b7fb568de01403bec144096b34a6bda4c581127bb3298765b2f6fbfa92",
            value.toSHA256(Charsets.UTF_16),
        )
    }
}
