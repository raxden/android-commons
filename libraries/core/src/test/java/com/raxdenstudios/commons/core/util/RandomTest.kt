package com.raxdenstudios.commons.core.util

import com.raxdenstudios.commons.core.util.Random.generateStringWithSize
import org.junit.Assert.assertTrue
import org.junit.Test

internal class RandomTest {

    @Test
    fun `generateStringWithSize should return a string with the size specified`() {
        val size = 12

        val result = generateStringWithSize(size)

        assertTrue(result.length == size)
    }
}
