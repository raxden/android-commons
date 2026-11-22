package com.raxdenstudios.commons.android.ext

import androidx.lifecycle.ViewModel
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import org.junit.Rule
import org.junit.Test

internal class ViewModelExtensionTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel = object : ViewModel() {}

    @Test
    fun `Given a block, When safeLaunch is called, Then it runs and the job completes`() {
        var executed = false

        val job = viewModel.safeLaunch { executed = true }

        assertThat(executed).isTrue()
        assertThat(job.isCompleted).isTrue()
    }

    @Test
    fun `Given a failing block, When safeLaunch is called, Then the error is swallowed`() {
        val job = viewModel.safeLaunch { error("boom") }

        assertThat(job.isCompleted).isTrue()
        assertThat(job.isCancelled).isTrue()
    }
}
