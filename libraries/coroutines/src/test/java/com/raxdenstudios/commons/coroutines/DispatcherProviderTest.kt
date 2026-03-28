package com.raxdenstudios.commons.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test

@ExperimentalCoroutinesApi
internal class DispatcherProviderTest {

    @Test
    fun `verify default implementation provides correct dispatchers`() {
        val dispatcherProvider = object : DispatcherProvider {
            override val main: CoroutineDispatcher = Dispatchers.Main
            override val io: CoroutineDispatcher = Dispatchers.IO
            override val default: CoroutineDispatcher = Dispatchers.Default
        }

        assertThat(dispatcherProvider.main).isEqualTo(Dispatchers.Main)
        assertThat(dispatcherProvider.io).isEqualTo(Dispatchers.IO)
        assertThat(dispatcherProvider.default).isEqualTo(Dispatchers.Default)
    }

    @Test
    fun `verify test implementation can use test dispatchers`() {
        val testDispatcher = kotlinx.coroutines.test.UnconfinedTestDispatcher()

        val dispatcherProvider = object : DispatcherProvider {
            override val main: CoroutineDispatcher = testDispatcher
            override val io: CoroutineDispatcher = testDispatcher
            override val default: CoroutineDispatcher = testDispatcher
        }

        assertThat(dispatcherProvider.main).isEqualTo(testDispatcher)
        assertThat(dispatcherProvider.io).isEqualTo(testDispatcher)
        assertThat(dispatcherProvider.default).isEqualTo(testDispatcher)
    }

    @Test
    fun `verify custom implementation can provide different dispatchers`() {
        val customMainDispatcher = kotlinx.coroutines.test.UnconfinedTestDispatcher()
        val customIoDispatcher = kotlinx.coroutines.test.StandardTestDispatcher()
        val customDefaultDispatcher = Dispatchers.Default

        val dispatcherProvider = object : DispatcherProvider {
            override val main: CoroutineDispatcher = customMainDispatcher
            override val io: CoroutineDispatcher = customIoDispatcher
            override val default: CoroutineDispatcher = customDefaultDispatcher
        }

        assertThat(dispatcherProvider.main).isEqualTo(customMainDispatcher)
        assertThat(dispatcherProvider.io).isEqualTo(customIoDispatcher)
        assertThat(dispatcherProvider.default).isEqualTo(customDefaultDispatcher)
    }
}
