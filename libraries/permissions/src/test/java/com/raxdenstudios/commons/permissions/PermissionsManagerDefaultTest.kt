package com.raxdenstudios.commons.permissions

import androidx.activity.ComponentActivity
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.permissions.model.Permission
import org.junit.Test

internal class PermissionsManagerDefaultTest {

    private class FakePermissionsManager : PermissionsManager {
        var receivedCallbacks: PermissionsManager.Callbacks? = null
        var receivedPermissions: List<Permission> = emptyList()

        override fun attach(
            activity: ComponentActivity,
        ) = Unit

        override fun requestPermission(
            callbacks: PermissionsManager.Callbacks,
            vararg permissions: Permission,
        ) {
            receivedCallbacks = callbacks
            receivedPermissions = permissions.toList()
        }

        override fun hasPermission(
            onGranted: (Boolean) -> Unit,
            permission: Permission,
        ) = Unit

        override fun shouldShowRationale(
            permission: Permission,
        ): Boolean = false
    }

    @Test
    fun `Given a manager without override When requestPermissionAfterRationale Then delegates to requestPermission`() {
        val manager = FakePermissionsManager()
        val callbacks = PermissionsManager.Callbacks()

        manager.requestPermissionAfterRationale(callbacks, Permission.Camera, Permission.RecordAudio)

        assertThat(manager.receivedCallbacks).isSameInstanceAs(callbacks)
        assertThat(manager.receivedPermissions).containsExactly(Permission.Camera, Permission.RecordAudio).inOrder()
    }
}
