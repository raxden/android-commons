package com.raxdenstudios.commons.permissions.model

import android.Manifest
import com.google.common.truth.Truth.assertThat
import org.junit.Test

internal class PermissionTest {

    @Test
    fun `fromValue should return Camera for camera permission`() {
        val result = Permission.fromValue(Manifest.permission.CAMERA)

        assertThat(result).isEqualTo(Permission.Camera)
    }

    @Test
    fun `fromValue should return AccessFineLocation for fine location permission`() {
        val result = Permission.fromValue(Manifest.permission.ACCESS_FINE_LOCATION)

        assertThat(result).isEqualTo(Permission.AccessFineLocation)
    }

    @Test
    fun `fromValue should return AccessCoarseLocation for coarse location permission`() {
        val result = Permission.fromValue(Manifest.permission.ACCESS_COARSE_LOCATION)

        assertThat(result).isEqualTo(Permission.AccessCoarseLocation)
    }

    @Test
    fun `fromValue should return ReadContacts for read contacts permission`() {
        val result = Permission.fromValue(Manifest.permission.READ_CONTACTS)

        assertThat(result).isEqualTo(Permission.ReadContacts)
    }

    @Test
    fun `fromValue should return WriteContacts for write contacts permission`() {
        val result = Permission.fromValue(Manifest.permission.WRITE_CONTACTS)

        assertThat(result).isEqualTo(Permission.WriteContacts)
    }

    @Test
    fun `fromValue should return RecordAudio for record audio permission`() {
        val result = Permission.fromValue(Manifest.permission.RECORD_AUDIO)

        assertThat(result).isEqualTo(Permission.RecordAudio)
    }

    @Test
    fun `fromValue should return CallPhone for call phone permission`() {
        val result = Permission.fromValue(Manifest.permission.CALL_PHONE)

        assertThat(result).isEqualTo(Permission.CallPhone)
    }

    @Test
    fun `fromValue should return ReadExternalStorage for read external storage permission`() {
        val result = Permission.fromValue(Manifest.permission.READ_EXTERNAL_STORAGE)

        assertThat(result).isEqualTo(Permission.ReadExternalStorage)
    }

    @Test
    fun `fromValue should return WriteExternalStorage for write external storage permission`() {
        val result = Permission.fromValue(Manifest.permission.WRITE_EXTERNAL_STORAGE)

        assertThat(result).isEqualTo(Permission.WriteExternalStorage)
    }

    @Test
    fun `fromValue should return Other for unknown permission`() {
        val unknownPermission = "android.permission.UNKNOWN"
        val result = Permission.fromValue(unknownPermission)

        assertThat(result).isInstanceOf(Permission.Other::class.java)
        assertThat((result as Permission.Other).permission).isEqualTo(unknownPermission)
    }

    @Test
    fun `Permission value should match manifest constant`() {
        assertThat(Permission.Camera.value).isEqualTo(Manifest.permission.CAMERA)
        assertThat(Permission.AccessFineLocation.value).isEqualTo(Manifest.permission.ACCESS_FINE_LOCATION)
        assertThat(Permission.AccessCoarseLocation.value).isEqualTo(Manifest.permission.ACCESS_COARSE_LOCATION)
        assertThat(Permission.ReadContacts.value).isEqualTo(Manifest.permission.READ_CONTACTS)
        assertThat(Permission.WriteContacts.value).isEqualTo(Manifest.permission.WRITE_CONTACTS)
        assertThat(Permission.RecordAudio.value).isEqualTo(Manifest.permission.RECORD_AUDIO)
        assertThat(Permission.CallPhone.value).isEqualTo(Manifest.permission.CALL_PHONE)
        assertThat(Permission.ReadExternalStorage.value).isEqualTo(Manifest.permission.READ_EXTERNAL_STORAGE)
        assertThat(Permission.WriteExternalStorage.value).isEqualTo(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    @Test
    fun `Other permission should have correct value`() {
        val customPermission = "com.example.CUSTOM_PERMISSION"
        val permission = Permission.Other(customPermission)

        assertThat(permission.value).isEqualTo(customPermission)
    }
}
