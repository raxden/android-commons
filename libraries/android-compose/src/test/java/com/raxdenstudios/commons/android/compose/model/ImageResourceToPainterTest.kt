package com.raxdenstudios.commons.android.compose.model

import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class ImageResourceToPainterTest {

    private val painters = mutableListOf<Painter>()

    private fun render(
        resource: ImageResource,
    ) {
        val activity = Robolectric.buildActivity(ComponentActivity::class.java).setup().get()
        activity.setContent { painters += resource.toPainter() }
        shadowOf(Looper.getMainLooper()).idle()
    }

    @Test
    fun `Given a Vector image When toPainter Then returns a painter`() {
        val vector = ImageVector.Builder(
            name = "square",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f,
        ).path(fill = SolidColor(Color.Black)) {
            lineTo(24f, 0f)
            lineTo(24f, 24f)
            close()
        }.build()

        render(ImageResource.Vector(vector))

        assertThat(painters).hasSize(1)
    }

    @Test
    fun `Given a Drawable image When toPainter Then returns a painter`() {
        render(ImageResource.Drawable(android.R.drawable.ic_menu_add))

        assertThat(painters).hasSize(1)
    }
}
