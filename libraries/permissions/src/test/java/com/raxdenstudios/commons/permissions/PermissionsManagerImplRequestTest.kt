package com.raxdenstudios.commons.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.savedstate.SavedStateRegistry
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.commons.permissions.model.Permission
import io.mockk.MockKVerificationScope
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class PermissionsManagerImplRequestTest {

    private val activity: ComponentActivity = mockk(relaxed = true)
    private val registry: ActivityResultRegistry = mockk()
    private val launcher: ActivityResultLauncher<Array<String>> = mockk(relaxed = true)
    private val keys = mutableListOf<String>()
    private val resultCallbacks = mutableListOf<ActivityResultCallback<Map<String, Boolean>>>()
    private val savedStateRegistry: SavedStateRegistry = mockk(relaxed = true)
    private val providers = mutableListOf<SavedStateRegistry.SavedStateProvider>()
    private val events = mutableListOf<String>()
    private lateinit var manager: PermissionsManagerImpl

    @Before
    fun setUp() {
        mockkStatic(ContextCompat::class)
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED
        every { activity.lifecycle } returns mockk<LifecycleRegistry>(relaxed = true)
        every { activity.activityResultRegistry } returns registry
        every { activity.savedStateRegistry } returns savedStateRegistry
        every { savedStateRegistry.registerSavedStateProvider(any(), capture(providers)) } just runs
        every {
            registry.register(
                capture(keys),
                any<LifecycleOwner>(),
                any<ActivityResultContract<Array<String>, Map<String, Boolean>>>(),
                capture(resultCallbacks),
            )
        } returns launcher
        manager = PermissionsManagerImpl()
        manager.attach(activity)
    }

    @After
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `Given rationale is needed, When requestPermission is called, Then onRationale is called, no launch`() {
        create()
        stubRationale(Permission.Camera, true)

        manager.requestPermission(callbacks(), Permission.Camera)

        assertThat(events).containsExactly("rationale:Camera")
        verify(exactly = 0) { launcher.launch(any<Array<String>>()) }
    }

    @Test
    fun `Given rationale is needed, When requestPermissionAfterRationale, Then it launches, no onRationale`() {
        create()
        stubRationale(Permission.Camera, true)

        manager.requestPermissionAfterRationale(callbacks(), Permission.Camera)

        assertThat(events).isEmpty()
        verify(exactly = 1) { launcher.launch(launched(Permission.Camera)) }
    }

    @Test
    fun `Given the permission is granted, When requestPermission is called, Then onGranted is called, no launch`() {
        create()
        stubGranted(Permission.Camera)
        stubRationale(Permission.Camera, true)

        manager.requestPermission(callbacks(), Permission.Camera)

        assertThat(events).containsExactly("granted:Camera")
        verify(exactly = 0) { launcher.launch(any<Array<String>>()) }
    }

    @Test
    fun `Given a pending request, When a second request is launched, Then the first gets onDenied exactly once`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)

        manager.requestPermission(callbacks("second"), Permission.RecordAudio)

        assertThat(events).containsExactly("first:denied:Camera")
    }

    @Test
    fun `Given a superseded request, When the result arrives, Then only the second callbacks receive it`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)
        manager.requestPermission(callbacks("second"), Permission.RecordAudio)
        events.clear()

        deliver(mapOf(Permission.RecordAudio.value to true))

        assertThat(events).containsExactly("second:granted:RecordAudio")
    }

    @Test
    fun `Given a pending request, When an empty result arrives, Then every pending permission is denied`() {
        create()
        manager.requestPermission(callbacks(), Permission.Camera, Permission.RecordAudio)

        deliver(emptyMap())

        assertThat(events).containsExactly("denied:Camera", "denied:RecordAudio").inOrder()
    }

    @Test
    fun `Given two launches, When a result for other permissions arrives, Then it is ignored until the later result`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)
        manager.requestPermission(callbacks("second"), Permission.Camera)
        events.clear()

        deliver(mapOf(Permission.RecordAudio.value to true))
        assertThat(events).isEmpty()
        deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("second:granted:Camera")
    }

    @Test
    fun `Given a single launch, When a result for other permissions arrives, Then the request is denied`() {
        create()
        manager.requestPermission(callbacks(), Permission.Camera)

        deliver(mapOf(Permission.RecordAudio.value to true))

        assertThat(events).containsExactly("denied:Camera")
    }

    @Test
    fun `Given a superseded launch, When empty result arrives, Then the real one is delivered`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)
        manager.requestPermission(callbacks("second"), Permission.RecordAudio)
        assertThat(events).containsExactly("first:denied:Camera")

        deliver(emptyMap())
        assertThat(events).containsExactly("first:denied:Camera")
        deliver(mapOf(Permission.RecordAudio.value to true))

        assertThat(events).containsExactly("first:denied:Camera", "second:granted:RecordAudio").inOrder()
    }

    @Test
    fun `Given two launches for the same permissions, When empty then first result come, Then second gets it`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)
        manager.requestPermission(callbacks("second"), Permission.Camera)
        events.clear()

        deliver(emptyMap())
        deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("second:granted:Camera")
    }

    @Test
    fun `Given two launches for different permissions, When empty then first result come, Then second is denied`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)
        manager.requestPermission(callbacks("second"), Permission.RecordAudio)
        events.clear()

        deliver(emptyMap())
        deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("second:denied:RecordAudio")
    }

    @Test
    fun `Given a destroyed owner, When a new launch gets an empty result, Then the new request is denied`() {
        create()
        manager.requestPermission(callbacks("old"), Permission.Camera)
        manager.onDestroy(activity)
        manager.attach(activity)
        manager.onCreate(activity)
        events.clear()

        manager.requestPermission(callbacks("new"), Permission.RecordAudio)
        deliver(emptyMap())

        assertThat(events).containsExactly("new:denied:RecordAudio")
    }

    @Test
    fun `Given a pending request, When a second call only has granted permissions, Then the pending one stays`() {
        create()
        stubGranted(Permission.RecordAudio)
        manager.requestPermission(callbacks("first"), Permission.Camera)

        manager.requestPermission(callbacks("second"), Permission.RecordAudio)
        assertThat(events).containsExactly("second:granted:RecordAudio")
        deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("second:granted:RecordAudio", "first:granted:Camera").inOrder()
    }

    @Test
    fun `Given a delivered result, When another result arrives, Then the callbacks are not called again`() {
        create()
        manager.requestPermission(callbacks(), Permission.Camera)
        deliver(mapOf(Permission.Camera.value to true))

        deliver(mapOf(Permission.Camera.value to false))

        assertThat(events).containsExactly("granted:Camera")
    }

    @Test
    fun `Given two managers on one registry, When both are created, Then keys are indexed 0 and 1`() {
        val other = PermissionsManagerImpl().apply { attach(activity) }

        manager.onCreate(activity)
        other.onCreate(activity)

        assertThat(keys).containsExactly("requestPermissions#0", "requestPermissions#1").inOrder()
    }

    @Test
    fun `Given one manager, When it registers again, Then it keeps the same key`() {
        manager.onCreate(activity)
        manager.onCreate(activity)

        assertThat(keys).containsExactly("requestPermissions#0", "requestPermissions#0").inOrder()
    }

    @Test
    fun `Given a fresh manager on a fresh registry, When it is created, Then its key is 0`() {
        manager.onCreate(activity)
        manager.onCreate(activity)
        val otherActivity: ComponentActivity = mockk(relaxed = true)
        val otherRegistry: ActivityResultRegistry = mockk()
        every { otherActivity.lifecycle } returns mockk<LifecycleRegistry>(relaxed = true)
        every { otherActivity.activityResultRegistry } returns otherRegistry
        every {
            otherRegistry.register(
                capture(keys),
                any<LifecycleOwner>(),
                any<ActivityResultContract<Array<String>, Map<String, Boolean>>>(),
                any<ActivityResultCallback<Map<String, Boolean>>>(),
            )
        } returns launcher
        val fresh = PermissionsManagerImpl().apply { attach(otherActivity) }

        fresh.onCreate(otherActivity)

        assertThat(keys.last()).isEqualTo("requestPermissions#0")
    }

    @Test
    fun `Given a pending request, When the owner is destroyed, Then it is denied and later requests get onDenied`() {
        create()
        manager.requestPermission(callbacks("first"), Permission.Camera)

        manager.onDestroy(activity)
        manager.requestPermission(callbacks("later"), Permission.RecordAudio)

        assertThat(events).containsExactly("first:denied:Camera", "later:denied:RecordAudio").inOrder()
    }

    @Test
    fun `Given a pending request, When the owner is destroyed for a configuration change, Then it stays pending`() {
        create()
        every { activity.isChangingConfigurations } returns true
        manager.requestPermission(callbacks(), Permission.Camera)

        val saved = providers.last().saveState()

        manager.onDestroy(activity)
        assertThat(events).isEmpty()
        val recreated = recreatedActivity(saved)
        manager.attach(recreated)
        manager.onCreate(recreated)
        resultCallbacks.last().onActivityResult(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("granted:Camera")
    }

    @Test
    fun `Given another owner, When it is destroyed, Then the launcher and the attached activity are kept`() {
        create()
        stubRationale(Permission.Camera, true)
        manager.requestPermission(callbacks("first"), Permission.RecordAudio)

        manager.onDestroy(mockk<LifecycleOwner>(relaxed = true))

        assertThat(events).isEmpty()
        assertThat(manager.shouldShowRationale(Permission.Camera)).isTrue()
        manager.requestPermission(callbacks("second"), Permission.AccessFineLocation)
        verify { launcher.launch(launched(Permission.AccessFineLocation)) }
    }

    private fun recreatedActivity(
        saved: Bundle,
    ): ComponentActivity {
        val recreated: ComponentActivity = mockk(relaxed = true)
        val recreatedRegistry: ActivityResultRegistry = mockk()
        val recreatedSavedState: SavedStateRegistry = mockk(relaxed = true)
        every { recreated.lifecycle } returns mockk<LifecycleRegistry>(relaxed = true)
        every { recreated.activityResultRegistry } returns recreatedRegistry
        every { recreated.savedStateRegistry } returns recreatedSavedState
        every { recreatedSavedState.isRestored } returns true
        every { recreatedSavedState.consumeRestoredStateForKey(any()) } returns saved
        every {
            recreatedRegistry.register(
                capture(keys),
                any<LifecycleOwner>(),
                any<ActivityResultContract<Array<String>, Map<String, Boolean>>>(),
                capture(resultCallbacks),
            )
        } returns launcher
        return recreated
    }

    private fun create() {
        manager.onCreate(activity)
    }

    private fun deliver(
        result: Map<String, Boolean>,
    ) {
        resultCallbacks.last().onActivityResult(result)
    }

    private fun stubGranted(
        permission: Permission,
    ) {
        every { ContextCompat.checkSelfPermission(any(), permission.value) } returns PackageManager.PERMISSION_GRANTED
    }

    private fun stubRationale(
        permission: Permission,
        value: Boolean,
    ) {
        every { activity.shouldShowRequestPermissionRationale(permission.value) } returns value
    }

    private fun MockKVerificationScope.launched(
        vararg permissions: Permission,
    ): Array<String> = match { input -> input.toList() == permissions.map { permission -> permission.value } }

    private fun callbacks(
        tag: String? = null,
    ): PermissionsManager.Callbacks {
        val prefix = tag?.let { "$it:" }.orEmpty()
        return PermissionsManager.Callbacks(
            onGranted = { events += "${prefix}granted:${it.name()}" },
            onRationale = { events += "${prefix}rationale:${it.name()}" },
            onDenied = { events += "${prefix}denied:${it.name()}" },
        )
    }

    private fun Permission.name(): String = this::class.simpleName.orEmpty()
}
