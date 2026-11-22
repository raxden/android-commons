package com.raxdenstudios.commons.pagination.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class PageIndexTest {

    @Test
    fun `Given the first index When read Then its value is zero`() {
        assertThat(PageIndex.first).isEqualTo(PageIndex(0))
    }

    @Test
    fun `Given Page and PageSize When compared Then equality is structural`() {
        assertThat(Page(1)).isEqualTo(Page(1))
        assertThat(Page(1)).isNotEqualTo(Page(2))
        assertThat(PageSize.defaultSize.value).isEqualTo(10)
    }
}
