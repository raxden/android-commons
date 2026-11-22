@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.android.property

import android.content.SharedPreferences
import com.google.common.truth.Truth.assertThat
import io.mockk.mockk
import org.junit.Assert.assertThrows
import org.junit.Test

internal class SharedPreferencesDelegateTest {

    private val preferences: SharedPreferences = mockk(relaxed = true)
    private val owner = Any()
    private val property = this::owner

    @Test
    fun `Given an Int default value, When getValue is called, Then throw IllegalStateException`() {
        val delegate = SharedPreferencesDelegate(preferences = preferences, defaultValue = 1)

        val error = assertThrows(IllegalStateException::class.java) { delegate.getValue(owner, property) }

        assertThat(error).hasMessageThat().isEqualTo("type not supported")
    }

    @Test
    fun `Given an Int default value, When setValue is called, Then throw IllegalStateException`() {
        val delegate = SharedPreferencesDelegate(preferences = preferences, defaultValue = 1)

        val error = assertThrows(IllegalStateException::class.java) { delegate.setValue(owner, property, 2) }

        assertThat(error).hasMessageThat().isEqualTo("type not supported")
    }
}
