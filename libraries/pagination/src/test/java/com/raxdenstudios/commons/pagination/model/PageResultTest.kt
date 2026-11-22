package com.raxdenstudios.commons.pagination.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class PageResultTest {

    @Test
    fun `Given NoResults When compared Then it is a singleton different from the other objects`() {
        assertThat(PageResult.NoResults).isSameInstanceAs(PageResult.NoResults)
        assertThat(PageResult.NoResults).isNotEqualTo(PageResult.NoMoreResults)
        assertThat(PageResult.NoResults).isNotEqualTo(PageResult.Loading)
        assertThat(PageResult.NoResults.toString()).isEqualTo("NoResults")
        assertThat(PageResult.NoResults.hashCode()).isEqualTo(PageResult.NoResults.hashCode())
    }

    @Test
    fun `Given Content and Error When compared Then equality is structural`() {
        val error = IllegalStateException("boom")

        assertThat(PageResult.Content(listOf(1))).isEqualTo(PageResult.Content(listOf(1)))
        assertThat(PageResult.Content(listOf(1))).isNotEqualTo(PageResult.Content(listOf(2)))
        assertThat(PageResult.Error(error)).isEqualTo(PageResult.Error(error))
        assertThat(PageResult.Error(error).throwable).isSameInstanceAs(error)
    }
}
