package com.raxdenstudios.commons.permissions.ext

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.permissions.PermissionsManager
import com.raxdenstudios.commons.permissions.PermissionsManagerImpl
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.commons.permissions.model.PermissionResult
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

internal class PermissionsManagerExtensionTest {

    private val permissionsManager: PermissionsManager = mockk(relaxed = true)
    private val callbacksSlot = slot<PermissionsManager.Callbacks>()

    @Test
    fun `Given the permission is granted, When requestPermission is called, Then return Granted`() = runTest {
        stubRequest { onGranted(Permission.Camera) }

        val result = permissionsManager.requestPermission(Permission.Camera)

        assertThat(result).isEqualTo(PermissionResult.Granted(Permission.Camera))
    }

    @Test
    fun `Given the permission is denied, When requestPermission is called, Then return Denied`() = runTest {
        stubRequest { onDenied(Permission.Camera) }

        val result = permissionsManager.requestPermission(Permission.Camera)

        assertThat(result).isEqualTo(PermissionResult.Denied(Permission.Camera))
    }

    @Test
    fun `Given a rationale is needed, When requestPermission is called, Then return Rationale`() = runTest {
        stubRequest { onRationale(Permission.Camera) }

        val result = permissionsManager.requestPermission(Permission.Camera)

        assertThat(result).isEqualTo(PermissionResult.Rationale(Permission.Camera))
    }

    @Test
    fun `Given several callbacks fire, When requestPermission is called, Then second does not throw`() = runTest {
        stubRequest {
            onRationale(Permission.Camera)
            onDenied(Permission.Camera)
        }

        val result = permissionsManager.requestPermission(Permission.Camera)

        assertThat(result).isEqualTo(PermissionResult.Rationale(Permission.Camera))
    }

    @Test
    fun `Given a manager that is not attached, When requestPermission is called, Then return Denied`() = runTest {
        val manager = PermissionsManagerImpl()

        val result = manager.requestPermission(Permission.Camera)

        assertThat(result).isEqualTo(PermissionResult.Denied(Permission.Camera))
    }

    @Test
    fun `Given rationale is needed, When requestPermissionAfterRationale is called, Then it never returns Rationale`() =
        runTest {
            val callbacks = slot<PermissionsManager.Callbacks>()
            every {
                permissionsManager.requestPermissionAfterRationale(capture(callbacks), Permission.Camera)
            } answers {
                callbacks.captured.onGranted(Permission.Camera)
            }

            val result = permissionsManager.requestPermissionAfterRationale(Permission.Camera)

            assertThat(result).isEqualTo(PermissionResult.Granted(Permission.Camera))
            verify(exactly = 0) { permissionsManager.requestPermission(any(), any()) }
        }

    @Test
    fun `Given a request superseded by another, When the first is awaited, Then it resumes with Denied`() = runTest {
        val captured = mutableListOf<PermissionsManager.Callbacks>()
        every { permissionsManager.requestPermission(capture(captured), Permission.Camera) } returns Unit
        val first = async { permissionsManager.requestPermission(Permission.Camera) }
        runCurrent()

        captured.single().onDenied(Permission.Camera)

        assertThat(first.await()).isEqualTo(PermissionResult.Denied(Permission.Camera))
    }

    @Test
    fun `Given a resumed request, When a later callback fires, Then it does not throw`() = runTest {
        val captured = mutableListOf<PermissionsManager.Callbacks>()
        every { permissionsManager.requestPermission(capture(captured), Permission.Camera) } returns Unit
        val first = async { permissionsManager.requestPermission(Permission.Camera) }
        runCurrent()
        captured.single().onDenied(Permission.Camera)

        captured.single().onGranted(Permission.Camera)

        assertThat(first.await()).isEqualTo(PermissionResult.Denied(Permission.Camera))
    }

    @Test
    fun `Given the permission is granted, When hasPermission is called, Then return true`() = runTest {
        every { permissionsManager.hasPermission(any(), Permission.Camera) } answers {
            firstArg<(Boolean) -> Unit>().invoke(true)
        }

        assertThat(permissionsManager.hasPermission(Permission.Camera)).isTrue()
    }

    @Test
    fun `Given the permission is not granted, When hasPermission is called, Then return false`() = runTest {
        every { permissionsManager.hasPermission(any(), Permission.Camera) } answers {
            firstArg<(Boolean) -> Unit>().invoke(false)
        }

        assertThat(permissionsManager.hasPermission(Permission.Camera)).isFalse()
    }

    private fun stubRequest(
        invoke: PermissionsManager.Callbacks.() -> Unit,
    ) {
        every { permissionsManager.requestPermission(capture(callbacksSlot), Permission.Camera) } answers {
            callbacksSlot.captured.invoke()
        }
    }
}
