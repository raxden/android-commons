@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.ext

import com.google.android.material.appbar.AppBarLayout
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class AppBarScrollProgressListenerTest {

    private val appBarLayout: AppBarLayout = mockk()
    private val progress = mutableListOf<Int>()
    private val listener = object : AppBarScrollProgressListener() {
        override fun onProgressChanged(
            appBarLayout: AppBarLayout,
            progress: Int,
        ) {
            this@AppBarScrollProgressListenerTest.progress += progress
        }
    }

    @Test
    fun `Given a total scroll range of zero, When the offset changes, Then progress is 0`() {
        every { appBarLayout.totalScrollRange } returns 0

        listener.onOffsetChanged(appBarLayout, -50)

        assertThat(progress).containsExactly(0)
    }

    @Test
    fun `Given a scroll range, When the offset changes, Then progress is a percentage`() {
        every { appBarLayout.totalScrollRange } returns 200

        listener.onOffsetChanged(appBarLayout, -50)
        listener.onOffsetChanged(appBarLayout, -200)

        assertThat(progress).containsExactly(25, 100).inOrder()
    }
}
