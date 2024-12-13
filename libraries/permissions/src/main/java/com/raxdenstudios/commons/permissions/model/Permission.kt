package com.raxdenstudios.commons.permissions.model

import android.Manifest

sealed class Permission(
    val value: String,
) {
    data object Camera : Permission(
        value = Manifest.permission.CAMERA
    )

    data object AccessFineLocation : Permission(
        value = Manifest.permission.ACCESS_FINE_LOCATION
    )

    /**
     * Read contacts
     *
     * @constructor Create empty Read contacts
     */
    data object ReadContacts : Permission(
        value = Manifest.permission.READ_CONTACTS,
    )

    data class Other(
        val permission: String
    ) : Permission(
        value = permission
    )

    companion object {

        /**
         * From value
         *
         * @param value
         * @return
         */
        fun fromValue(value: String): Permission = when (value) {
            Manifest.permission.CAMERA -> Camera
            Manifest.permission.ACCESS_FINE_LOCATION -> AccessFineLocation
            Manifest.permission.READ_CONTACTS -> ReadContacts
            else -> Other(value)
        }
    }
}
