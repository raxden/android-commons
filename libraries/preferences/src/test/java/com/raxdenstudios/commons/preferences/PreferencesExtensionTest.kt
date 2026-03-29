package com.raxdenstudios.commons.preferences

import android.content.Context
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class PreferencesExtensionTest {

    private lateinit var preferences: AdvancedPreferences

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        preferences = AdvancedPreferences.Default(context)
        // Clear preferences before each test
        preferences.edit { clear() }
    }

    @Test
    fun `edit extension with apply should persist value`() {
        preferences.edit {
            put("test_key", "test_value")
        }

        assertEquals("test_value", preferences.get("test_key", "default"))
    }

    @Test
    fun `edit extension with commit should persist value`() {
        preferences.edit(commit = true) {
            put("test_key", "test_value")
        }

        assertEquals("test_value", preferences.get("test_key", "default"))
    }

    @Test
    fun `edit extension should allow multiple operations`() {
        preferences.edit {
            put("key1", "value1")
            put("key2", 42)
            put("key3", true)
        }

        assertEquals("value1", preferences.get("key1", ""))
        assertEquals(42, preferences.get("key2", 0))
        assertEquals(true, preferences.get("key3", false))
    }

    @Test
    fun `edit extension with commit true should use commit`() {
        // This test verifies that commit = true works
        val result = preferences.edit(commit = true) {
            put("key", "value")
        }

        // Verify the value was persisted
        assertEquals("value", preferences.get("key", ""))
    }

    @Test
    fun `edit extension with commit false should use apply`() {
        // This test verifies that commit = false (default) works
        preferences.edit(commit = false) {
            put("key", "value")
        }

        // Verify the value was persisted
        assertEquals("value", preferences.get("key", ""))
    }

    @Test
    fun `edit extension should support remove operation`() {
        preferences.edit {
            put("key1", "value1")
            put("key2", "value2")
        }

        preferences.edit {
            remove("key1")
        }

        assertEquals("default", preferences.get("key1", "default"))
        assertEquals("value2", preferences.get("key2", "default"))
    }

    @Test
    fun `edit extension should support clear operation`() {
        preferences.edit {
            put("key1", "value1")
            put("key2", "value2")
            put("key3", "value3")
        }

        preferences.edit {
            clear()
        }

        assertEquals(false, preferences.contains("key1"))
        assertEquals(false, preferences.contains("key2"))
        assertEquals(false, preferences.contains("key3"))
    }
}
