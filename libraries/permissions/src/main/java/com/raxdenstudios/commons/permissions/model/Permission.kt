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

    data object AccessCoarseLocation : Permission(
        value = Manifest.permission.ACCESS_COARSE_LOCATION
    )

    data object ReadContacts : Permission(
        value = Manifest.permission.READ_CONTACTS,
    )

    data object WriteContacts : Permission(
        value = Manifest.permission.WRITE_CONTACTS,
    )

    data object RecordAudio : Permission(
        value = Manifest.permission.RECORD_AUDIO,
    )

    data object CallPhone : Permission(
        value = Manifest.permission.CALL_PHONE,
    )

    data object ReadExternalStorage : Permission(
        value = Manifest.permission.READ_EXTERNAL_STORAGE,
    )

    data object WriteExternalStorage : Permission(
        value = Manifest.permission.WRITE_EXTERNAL_STORAGE,
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
            Manifest.permission.ACCESS_COARSE_LOCATION -> AccessCoarseLocation
            Manifest.permission.READ_CONTACTS -> ReadContacts
            Manifest.permission.WRITE_CONTACTS -> WriteContacts
            Manifest.permission.RECORD_AUDIO -> RecordAudio
            Manifest.permission.CALL_PHONE -> CallPhone
            Manifest.permission.READ_EXTERNAL_STORAGE -> ReadExternalStorage
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> WriteExternalStorage
            else -> Other(value)
        }
    }
}
