package com.raxdenstudios.commons.permissions.model

import androidx.annotation.StringRes
import com.raxdenstudios.commons.permissions.R

sealed class RationaleDialog(
    @field:StringRes val reason: Int,
    @field:StringRes val reasonDescription: Int,
    @field:StringRes val acceptLabel: Int,
    @field:StringRes val deniedLabel: Int
) {

    data object Camera : RationaleDialog(
        R.string.permission_rationale_camera_title,
        R.string.permission_rationale_camera_message,
        R.string.permission_rationale_camera_positive,
        R.string.permission_rationale_camera_negative,
    )

    data object AccessFineLocation : RationaleDialog(
        R.string.permission_rationale_access_fine_location_title,
        R.string.permission_rationale_access_fine_location_message,
        R.string.permission_rationale_access_fine_location_positive,
        R.string.permission_rationale_access_fine_location_negative,
    )
}
