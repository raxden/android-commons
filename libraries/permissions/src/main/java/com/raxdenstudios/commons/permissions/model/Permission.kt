package com.raxdenstudios.commons.permissions.model

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi

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

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    data object PostNotifications : Permission(
        value = Manifest.permission.POST_NOTIFICATIONS,
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
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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
            Manifest.permission.POST_NOTIFICATIONS -> PostNotifications
            else -> Other(value)
        }
    }
}
