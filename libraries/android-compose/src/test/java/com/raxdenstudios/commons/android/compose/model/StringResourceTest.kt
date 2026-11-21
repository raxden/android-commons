package com.raxdenstudios.commons.android.compose.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class StringResourceTest {

    @Test
    fun `Given ResourceId without params When created Then params are empty`() {
        assertThat(StringResource.ResourceId(resourceId = 1).params).isEmpty()
    }

    @Test
    fun `Given same id and params When compared Then ResourceIds are equal`() {
        val a = StringResource.ResourceId(resourceId = 1, params = listOf("a"))
        val b = StringResource.ResourceId(resourceId = 1, params = listOf("a"))

        assertThat(a).isEqualTo(b)
        assertThat(a.hashCode()).isEqualTo(b.hashCode())
    }

    @Test
    fun `Given different params When compared Then ResourceIds are not equal`() {
        val a = StringResource.ResourceId(resourceId = 1, params = listOf("a"))

        assertThat(a).isNotEqualTo(a.copy(params = listOf("b")))
        assertThat(a).isNotEqualTo(a.copy(resourceId = 2))
    }

    @Test
    fun `Given a text When PlainText is created Then it exposes the value`() {
        assertThat(StringResource.PlainText("hello").value).isEqualTo("hello")
        assertThat(StringResource.PlainText("hello")).isEqualTo(StringResource.PlainText("hello"))
        assertThat(StringResource.PlainText("hello")).isNotEqualTo(StringResource.PlainText("bye"))
    }

    @Test
    fun `Given Empty When referenced twice Then it is the same singleton`() {
        assertThat(StringResource.Empty).isSameInstanceAs(StringResource.Empty)
        assertThat(StringResource.Empty).isNotEqualTo(StringResource.PlainText(""))
    }
}
