package com.raxdenstudios.commons.android.compose.ext

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class ModifierTest {

    @Test
    fun `Given a modifier When mirror Then it scales horizontally by minus one`() {
        assertThat(Modifier.mirror()).isEqualTo(Modifier.scale(scaleX = -1f, scaleY = 1f))
        assertThat(Modifier.mirror()).isNotEqualTo(Modifier)
    }

    @Test
    fun `Given a modifier When aspectRatio11 Then ratio is one to one`() {
        assertThat(Modifier.aspectRatio11()).isEqualTo(Modifier.aspectRatio(1f))
    }

    @Test
    fun `Given a modifier When aspectRatio43 Then ratio is four to three`() {
        assertThat(Modifier.aspectRatio43()).isEqualTo(Modifier.aspectRatio(4f / 3f))
    }

    @Test
    fun `Given a modifier When aspectRatio169 Then ratio is sixteen to nine`() {
        assertThat(Modifier.aspectRatio169()).isEqualTo(Modifier.aspectRatio(16f / 9f))
    }

    @Test
    fun `Given a modifier When aspectRatio23 Then ratio is two to three`() {
        assertThat(Modifier.aspectRatio23()).isEqualTo(Modifier.aspectRatio(2f / 3f))
    }

    @Test
    fun `Given the aspect ratio modifiers When compared Then they are all different`() {
        val modifiers = listOf(
            Modifier.aspectRatio11(),
            Modifier.aspectRatio43(),
            Modifier.aspectRatio169(),
            Modifier.aspectRatio23(),
        )

        assertThat(modifiers.toSet()).hasSize(4)
    }
}
