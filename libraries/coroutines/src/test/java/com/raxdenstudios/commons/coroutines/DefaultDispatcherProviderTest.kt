package com.raxdenstudios.commons.coroutines

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import org.junit.Test

internal class DefaultDispatcherProviderTest {

    private val provider = DefaultDispatcherProvider()

    @Test
    fun `Given default provider When reading main Then returns Dispatchers Main`() {
        assertThat(provider.main).isSameInstanceAs(Dispatchers.Main)
    }

    @Test
    fun `Given default provider When reading io Then returns Dispatchers IO`() {
        assertThat(provider.io).isSameInstanceAs(Dispatchers.IO)
    }

    @Test
    fun `Given default provider When reading default Then returns Dispatchers Default`() {
        assertThat(provider.default).isSameInstanceAs(Dispatchers.Default)
    }
}
