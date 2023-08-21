package com.raxdenstudios.commons.ext

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.os.Environment
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

internal class ContextExtensionTest {

    private val context: Context = mockk(relaxed = true)
    private val connectivityManager: ConnectivityManager = mockk(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(File::class)
    }

    @After
    fun after() {
        unmockkStatic(File::class)
    }

    @Test
    fun `isNetworkConnected should return true`() {
        every { context.getSystemService(Context.CONNECTIVITY_SERVICE) } returns connectivityManager

        val result = context.isNetworkConnected()

        assertTrue(result)
    }

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
            result
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

    @Test
    fun `createTemporalImageFile should return a file`() {
        val context = mockk<ContextWrapper>()
        val storageDir = mockk<File>()
        val temporalFile = mockk<File>()
        every { context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) } returns storageDir
        every { File.createTempFile(any(), any(), any()) } returns temporalFile

        val result = context.createTemporalImageFile()

        assertEquals(temporalFile, result)
    }
}
