package com.raxdenstudios.commons.permissions.model

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.permissions.R
import org.junit.Test

internal class RationaleDialogTest {

    @Test
    fun `Given Camera dialog When reading its labels Then returns the camera strings`() {
        val dialog: RationaleDialog = RationaleDialog.Camera

        assertThat(dialog.reason).isEqualTo(R.string.permission_rationale_camera_title)
        assertThat(dialog.reasonDescription).isEqualTo(R.string.permission_rationale_camera_message)
        assertThat(dialog.acceptLabel).isEqualTo(R.string.permission_rationale_camera_positive)
        assertThat(dialog.deniedLabel).isEqualTo(R.string.permission_rationale_camera_negative)
    }

    @Test
    fun `Given AccessFineLocation dialog When reading its labels Then returns the location strings`() {
        val dialog: RationaleDialog = RationaleDialog.AccessFineLocation

        assertThat(dialog.reason).isEqualTo(R.string.permission_rationale_access_fine_location_title)
        assertThat(dialog.reasonDescription).isEqualTo(R.string.permission_rationale_access_fine_location_message)
        assertThat(dialog.acceptLabel).isEqualTo(R.string.permission_rationale_access_fine_location_positive)
        assertThat(dialog.deniedLabel).isEqualTo(R.string.permission_rationale_access_fine_location_negative)
    }

    @Test
    fun `Given the two dialogs When comparing Then they are different objects`() {
        assertThat(RationaleDialog.Camera).isNotEqualTo(RationaleDialog.AccessFineLocation)
    }
}
