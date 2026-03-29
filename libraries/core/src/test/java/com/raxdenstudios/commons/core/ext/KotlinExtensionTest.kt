package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class KotlinExtensionTest {

    @Test
    fun `exhaustive should return the same value when not null`() {
        val value: String? = "test"
        
        val result = value.exhaustive
        
        assertThat(result).isEqualTo("test")
    }

    @Test
    fun `exhaustive should return null when value is null`() {
        val value: String? = null
        
        val result = value.exhaustive
        
        assertThat(result).isNull()
    }

    @Test
    fun `exhaustive should work with when expressions on boolean`() {
        val value: Boolean = true

        val output = when (value) {
            true -> "success"
            false -> "failure"
        }.exhaustive

        assertThat(output).isEqualTo("success")
    }

    @Test
    fun `exhaustive should work with nullable when expressions`() {
        val value: String? = "test"

        val result = when (value) {
            null -> null
            else -> value.uppercase()
        }.exhaustive

        assertThat(result).isEqualTo("TEST")
    }

    @Test
    fun `exhaustive should preserve type information`() {
        val number: Int? = 42
        
        val result: Int? = number.exhaustive
        
        assertThat(result).isEqualTo(42)
    }

    @Test
    fun `exhaustive should work with different types`() {
        val stringValue: String? = "hello".exhaustive
        val intValue: Int? = 123.exhaustive
        val booleanValue: Boolean? = true.exhaustive
        val listValue: List<String>? = listOf("a", "b").exhaustive

        assertThat(stringValue).isEqualTo("hello")
        assertThat(intValue).isEqualTo(123)
        assertThat(booleanValue).isTrue()
        assertThat(listValue).containsExactly("a", "b")
    }

    @Test
    fun `exhaustive should help ensure when expressions are exhaustive`() {
        val value: Boolean = true

        // The exhaustive property ensures the when expression is exhaustive
        val result = when (value) {
            true -> "yes"
            false -> "no"
        }.exhaustive

        assertThat(result).isEqualTo("yes")
    }
}
