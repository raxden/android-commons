package com.raxdenstudios.commons.android.provider

import android.content.Context
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class StringProviderTest {

    private val context: Context = mockk()
    private val provider = StringProvider(context)

    @Test
    fun `Given a resource id When getting string Then delegates to context`() {
        every { context.getString(1) } returns "hello"

        assertThat(provider.getString(1)).isEqualTo("hello")
    }

    @Test
    fun `Given a resource id and args When getting string Then delegates with format args`() {
        every { context.getString(2, "a", 3) } returns "a-3"

        assertThat(provider.getString(2, "a", 3)).isEqualTo("a-3")
    }
}
