package com.raxdenstudios.commons.coroutines.test

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class TestDispatcherProviderTest {

    @Test
    fun `Given default constructor When reading dispatchers Then all are the same dispatcher`() {
        val provider = TestDispatcherProvider()

        assertThat(provider.main).isSameInstanceAs(provider.dispatcher)
        assertThat(provider.io).isSameInstanceAs(provider.dispatcher)
        assertThat(provider.default).isSameInstanceAs(provider.dispatcher)
    }

    @Test
    fun `Given custom dispatcher When reading dispatchers Then all return it`() {
        val dispatcher = StandardTestDispatcher()

        val provider = TestDispatcherProvider(dispatcher)

        assertThat(provider.dispatcher).isSameInstanceAs(dispatcher)
        assertThat(provider.main).isSameInstanceAs(dispatcher)
        assertThat(provider.io).isSameInstanceAs(dispatcher)
        assertThat(provider.default).isSameInstanceAs(dispatcher)
    }
}
