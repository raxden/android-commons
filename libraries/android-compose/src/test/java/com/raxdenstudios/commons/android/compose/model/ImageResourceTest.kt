package com.raxdenstudios.commons.android.compose.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class ImageResourceTest {

    @Test
    fun `Given same drawable id When compared Then Drawables are equal`() {
        assertThat(ImageResource.Drawable(1)).isEqualTo(ImageResource.Drawable(1))
        assertThat(ImageResource.Drawable(1).hashCode()).isEqualTo(ImageResource.Drawable(1).hashCode())
    }

    @Test
    fun `Given different drawable id When compared Then Drawables are not equal`() {
        assertThat(ImageResource.Drawable(1)).isNotEqualTo(ImageResource.Drawable(2))
    }
}
