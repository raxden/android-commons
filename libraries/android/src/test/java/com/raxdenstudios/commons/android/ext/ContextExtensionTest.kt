package com.raxdenstudios.commons.android.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

internal class ContextExtensionTest {

    private val context: Context = mockk(relaxed = true)
    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)

    @Test
    fun `getConnectivityManager should return valid data`() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        val result = context.getConnectivityManager()

        assertEquals(connectivityManager, result)
    }

    @Test
    fun `getPackageInfo should return valid data`() {
        val result = context.getPackageInfo()

        assertEquals(
            context.packageManager.getPackageInfo(context.packageName, 0),
            result,
        )
    }

    @Test
    fun `findActivity should return valid data`() {
        val context = mockk<Activity>()

        val result = context.findActivity()

        assertTrue(result != null)
    }

    @Test
    fun `given a context, when findActivity is called Then return valid data`() {
        val context = mockk<ContextWrapper>()
        every { context.baseContext } returns mockk<Activity>()

        val result = context.findActivity()

        assertTrue(result != null)
    }
}
