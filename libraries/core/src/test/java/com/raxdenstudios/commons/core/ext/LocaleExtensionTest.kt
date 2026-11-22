@file:Suppress("DEPRECATION")

package com.raxdenstudios.commons.core.ext

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.util.Locale

internal class LocaleExtensionTest {

    @Test
    fun `Given English locale and Spanish target, When displayLanguageName is called, Then no capitalisation`() {
        val result = Locale.ENGLISH.displayLanguageName(Locale.forLanguageTag("es"))

        assertThat(result).isEqualTo("inglés")
    }

    @Test
    fun `Given Spanish locale and English target, When displayLanguageName is called, Then return Spanish`() {
        val result = Locale.forLanguageTag("es").displayLanguageName(Locale.ENGLISH)

        assertThat(result).isEqualTo("Spanish")
    }
}
