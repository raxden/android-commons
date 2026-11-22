package com.raxdenstudios.commons.android.ext

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.assertThrows
import org.junit.Test

internal class SavedStateHandleExtensionTest {

    private val handle = SavedStateHandle(mapOf("key" to "value"))

    @Test
    fun `Given existing key When getOrElse Then returns value`() {
        val result: String = handle.getOrElse<String, String>("key") { "fallback" }

        assertThat(result).isEqualTo("value")
    }

    @Test
    fun `Given missing key When getOrElse Then returns onFailure result`() {
        val result: String = handle.getOrElse<String, String>("missing") { "fallback" }

        assertThat(result).isEqualTo("fallback")
    }

    @Test
    fun `Given existing key When getOrDefault Then returns value`() {
        assertThat(handle.getOrDefault("key", "default")).isEqualTo("value")
    }

    @Test
    fun `Given missing key When getOrDefault Then returns default`() {
        assertThat(handle.getOrDefault("missing", "default")).isEqualTo("default")
    }

    @Test
    fun `Given existing key When getOrThrow Then returns value`() {
        assertThat(handle.getOrThrow<String>("key")).isEqualTo("value")
    }

    @Test
    fun `Given missing key When getOrThrow Then throws`() {
        val error = assertThrows(IllegalStateException::class.java) {
            handle.getOrThrow<String>("missing")
        }

        assertThat(error).hasMessageThat().isEqualTo("No value for key 'missing'")
    }

    @Test
    fun `Given existing key When getOrThrow with message Then returns value`() {
        assertThat(handle.getOrThrow<String>("key", "boom")).isEqualTo("value")
    }

    @Test
    fun `Given missing key When getOrThrow with message Then throws with message`() {
        val error = assertThrows(IllegalStateException::class.java) {
            handle.getOrThrow<String>("missing", "boom")
        }

        assertThat(error).hasMessageThat().isEqualTo("boom")
    }

    @Test
    fun `Given existing key When getOrNull Then returns value`() {
        assertThat(handle.getOrNull<String>("key")).isEqualTo("value")
    }

    @Test
    fun `Given missing key When getOrNull Then returns null`() {
        assertThat(handle.getOrNull<String>("missing")).isNull()
    }

    @Test
    fun `Given wrong type When getOrElse Then onFailure receives IllegalStateException`() {
        var failure: Throwable? = null

        val result: Int = handle.getOrElse<Int, Int>("key") {
            failure = it
            -1
        }

        assertThat(result).isEqualTo(-1)
        assertThat(failure).isInstanceOf(IllegalStateException::class.java)
    }

    @Test
    fun `Given wrong type When getOrDefault Then returns default`() {
        assertThat(handle.getOrDefault("key", 5)).isEqualTo(5)
    }

    @Test
    fun `Given wrong type When getOrThrow Then throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) { handle.getOrThrow<Int>("key") }
    }

    @Test
    fun `Given wrong type When getOrThrow with message Then throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) { handle.getOrThrow<Int>("key", "boom") }
    }

    @Test
    fun `Given wrong type When getOrNull Then returns null`() {
        assertThat(handle.getOrNull<Int>("key")).isNull()
    }
}
