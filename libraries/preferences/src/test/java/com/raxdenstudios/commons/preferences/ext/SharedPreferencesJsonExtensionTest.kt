package com.raxdenstudios.commons.preferences.ext

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.test.core.app.ApplicationProvider
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertSame
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class SharedPreferencesJsonExtensionTest {

    @Serializable
    private data class Sample(
        val name: String,
        val age: Int,
    )

    private lateinit var preferences: SharedPreferences

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        preferences = context.getSharedPreferences("json_ext_test", Context.MODE_PRIVATE)
        preferences.edit().clear().commit()
    }

    @Test
    fun `Given missing key When getJson Then returns null`() {
        assertNull(preferences.getJson<Sample>("missing"))
    }

    @Test
    fun `Given invalid json When getJson Then returns null`() {
        preferences.edit().putString("key", "not json").commit()

        assertNull(preferences.getJson<Sample>("key"))
    }

    @Test
    fun `Given stored value When getJson Then round trips with putJson`() {
        val sample = Sample(name = "Ana", age = 30)

        preferences.edit().putJson("key", sample).commit()

        assertEquals(sample, preferences.getJson<Sample>("key"))
    }

    @Test
    fun `Given unknown keys and custom json When getJson Then decodes ignoring unknown keys`() {
        preferences.edit().putString("key", """{"name":"Ana","age":30,"extra":true}""").commit()
        val lenient = Json { ignoreUnknownKeys = true }

        assertNull(preferences.getJson<Sample>("key"))
        assertEquals(Sample("Ana", 30), preferences.getJson<Sample>("key", lenient))
    }

    @Test
    fun `Given editor When putJson Then returns the same editor`() {
        val editor = preferences.edit()

        assertSame(editor, editor.putJson("key", Sample("Ana", 30)))
    }

    @Test
    fun `Given editor When putOrRemove Then returns the same editor`() {
        val editor = preferences.edit()

        assertSame(editor, editor.putOrRemove("key", "value"))
        assertSame(editor, editor.putOrRemove("key", null))
    }

    @Test
    fun `Given non null value When putOrRemove Then stores it`() {
        preferences.edit().putOrRemove("key", "value").commit()

        assertEquals("value", preferences.getString("key", null))
    }

    @Test
    fun `Given stored key When putOrRemove null Then removes it`() {
        preferences.edit().putString("key", "value").commit()

        preferences.edit().putOrRemove("key", null).commit()

        assertFalse(preferences.contains("key"))
        assertTrue(preferences.all.isEmpty())
    }
}
