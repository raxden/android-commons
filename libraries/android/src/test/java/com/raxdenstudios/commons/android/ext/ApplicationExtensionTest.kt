package com.raxdenstudios.commons.android.ext

import android.app.Application
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ApplicationExtensionTest {

    private val application: Application = RuntimeEnvironment.getApplication()

    @Test
    fun `Given an application When initCompatVector Then does not throw`() {
        application.initCompatVector()
    }
}
