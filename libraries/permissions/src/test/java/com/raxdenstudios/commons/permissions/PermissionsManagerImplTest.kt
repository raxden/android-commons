package com.raxdenstudios.commons.permissions

import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleRegistry
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.permissions.model.Permission
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test

internal class PermissionsManagerImplTest {

    private lateinit var permissionsManager: PermissionsManagerImpl
    private val activity: ComponentActivity = mockk(relaxed = true)
    private val lifecycle: LifecycleRegistry = mockk(relaxed = true)

    @Before
    fun setUp() {
        permissionsManager = PermissionsManagerImpl()
        every { activity.lifecycle } returns lifecycle
    }

    @Test
    fun `attach should add lifecycle observer`() {
        permissionsManager.attach(activity)

        verify { lifecycle.addObserver(permissionsManager) }
    }

    @Test
    fun `Callbacks should have default empty implementations`() {
        val callbacks = PermissionsManager.Callbacks()

        // Should not throw exceptions
        callbacks.onGranted(Permission.Camera)
        callbacks.onRationale(Permission.Camera)
        callbacks.onDenied(Permission.Camera)
    }

    @Test
    fun `Callbacks should execute custom implementations`() {
        var grantedCalled = false
        var rationaleCalled = false
        var deniedCalled = false

        val callbacks = PermissionsManager.Callbacks(
            onGranted = { grantedCalled = true },
            onRationale = { rationaleCalled = true },
            onDenied = { deniedCalled = true }
        )

        callbacks.onGranted(Permission.Camera)
        callbacks.onRationale(Permission.Camera)
        callbacks.onDenied(Permission.Camera)

        assertThat(grantedCalled).isTrue()
        assertThat(rationaleCalled).isTrue()
        assertThat(deniedCalled).isTrue()
    }

    @Test
    fun `PermissionsManagerImpl should be instantiable`() {
        val manager = PermissionsManagerImpl()

        assertThat(manager).isNotNull()
    }

    @Test
    fun `Callbacks with partial implementations should work`() {
        var grantedCalled = false

        val callbacks = PermissionsManager.Callbacks(
            onGranted = { grantedCalled = true }
        )

        callbacks.onGranted(Permission.Camera)
        callbacks.onRationale(Permission.Camera) // Should not throw
        callbacks.onDenied(Permission.Camera) // Should not throw

        assertThat(grantedCalled).isTrue()
    }
}
