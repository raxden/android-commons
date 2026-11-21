package com.raxdenstudios.commons.coroutines.test

import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.TestScope
import org.junit.Test

internal class TestCoroutineScopeProviderTest {

    @Test
    fun `Given a scope When reading main io and default Then all return that scope`() {
        val scope = TestScope()

        val provider = TestCoroutineScopeProvider(scope)

        assertThat(provider.scope).isSameInstanceAs(scope)
        assertThat(provider.main).isSameInstanceAs(scope)
        assertThat(provider.io).isSameInstanceAs(scope)
        assertThat(provider.default).isSameInstanceAs(scope)
    }
}
