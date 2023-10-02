package com.raxdenstudios.commons.core.ext

import com.raxdenstudios.commons.core.ext.orDefault
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

internal class BooleanExtensionTest {

    @Test
    fun `null orDefault should return default value`() {
        val nullValue: Boolean? = null

        assertFalse(nullValue.orDefault())
        assertFalse(nullValue.orDefault(false))
        assertTrue(true.orDefault(false))
    }
}
