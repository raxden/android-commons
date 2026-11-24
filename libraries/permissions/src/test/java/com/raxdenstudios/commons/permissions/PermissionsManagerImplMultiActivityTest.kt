package com.raxdenstudios.commons.permissions

import android.content.Context
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
import com.raxdenstudios.commons.permissions.ext.requestPermission
import com.raxdenstudios.commons.permissions.model.Permission
import com.raxdenstudios.commons.permissions.model.PermissionResult
import io.mockk.MockKVerificationScope
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.async
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class PermissionsManagerImplMultiActivityTest {

    private val events = mutableListOf<String>()
    private val appContext: Context = mockk(relaxed = true)
    private lateinit var manager: PermissionsManagerImpl
    private lateinit var activityA: FakeActivity
    private lateinit var activityB: FakeActivity

    @Before
    fun setUp() {
        mockkStatic(ContextCompat::class)
        every { ContextCompat.checkSelfPermission(any(), any()) } returns PackageManager.PERMISSION_DENIED
        every { appContext.applicationContext } returns appContext
        manager = PermissionsManagerImpl(appContext)
        activityA = FakeActivity()
        activityB = FakeActivity()
    }

    @After
    fun tearDown() {
        unmockkStatic(ContextCompat::class)
    }

    @Test
    fun `Given A then B created, When requestPermission is called, Then it launches on B and not on A`() {
        attachAndCreate(activityA, activityB)

        manager.requestPermission(callbacks(), Permission.Camera)

        verify(exactly = 1) { activityB.launcher.launch(launched(Permission.Camera)) }
        verify(exactly = 0) { activityA.launcher.launch(any<Array<String>>()) }
    }

    @Test
    fun `Given A then B created, When shouldShowRationale and hasPermission are called, Then B is used`() {
        attachAndCreate(activityA, activityB)
        activityA.stubRationale(Permission.Camera, false)
        activityB.stubRationale(Permission.Camera, true)
        stubGranted(activityB, Permission.Camera)

        assertThat(manager.shouldShowRationale(Permission.Camera)).isTrue()
        assertThat(hasPermission(Permission.Camera)).isTrue()
    }

    @Test
    fun `Given B destroyed for good, When requests and checks are made, Then they fall back to A`() {
        attachAndCreate(activityA, activityB)
        activityA.stubRationale(Permission.Camera, true)
        activityB.stubRationale(Permission.Camera, false)
        stubGranted(activityA, Permission.Camera)

        manager.onDestroy(activityB.activity)
        manager.requestPermission(callbacks(), Permission.RecordAudio)

        verify(exactly = 1) { activityA.launcher.launch(launched(Permission.RecordAudio)) }
        verify(exactly = 0) { activityB.launcher.launch(any<Array<String>>()) }
        assertThat(manager.shouldShowRationale(Permission.Camera)).isTrue()
        assertThat(hasPermission(Permission.Camera)).isTrue()
    }

    @Test
    fun `Given the top isDestroyed before onDestroy, When requests and checks are made, Then A is used`() {
        attachAndCreate(activityA, activityB)
        activityA.stubRationale(Permission.Camera, true)
        stubGranted(activityA, Permission.Camera)
        every { activityB.activity.isDestroyed } returns true

        manager.requestPermission(callbacks(), Permission.RecordAudio)

        verify(exactly = 1) { activityA.launcher.launch(launched(Permission.RecordAudio)) }
        verify(exactly = 0) { activityB.launcher.launch(any<Array<String>>()) }
        assertThat(manager.shouldShowRationale(Permission.Camera)).isTrue()
        assertThat(hasPermission(Permission.Camera)).isTrue()
    }

    @Test
    fun `Given a request launched on B, When B is destroyed for good, Then it is denied and later ones use A`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)

        manager.onDestroy(activityB.activity)
        assertThat(events).containsExactly("first:denied:Camera")
        manager.requestPermission(callbacks("later"), Permission.RecordAudio)

        verify(exactly = 1) { activityA.launcher.launch(launched(Permission.RecordAudio)) }
        activityA.deliver(mapOf(Permission.RecordAudio.value to true))
        assertThat(events).containsExactly("first:denied:Camera", "later:granted:RecordAudio").inOrder()
    }

    @Test
    fun `Given a request launched on A, When B is destroyed, Then it is not denied and A result delivers it`() {
        attachAndCreate(activityA)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        attachAndCreate(activityB)

        manager.onDestroy(activityB.activity)
        assertThat(events).isEmpty()
        activityA.deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("first:granted:Camera")
    }

    @Test
    fun `Given a request launched on B, When A callback gets a result, Then it is ignored and B result delivers`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks(), Permission.Camera)

        activityA.deliver(mapOf(Permission.Camera.value to true))
        assertThat(events).isEmpty()
        activityB.deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("granted:Camera")
    }

    @Test
    fun `Given a request on A then on B, When late A results arrive, Then they are ignored and B result delivers`() {
        attachAndCreate(activityA)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        attachAndCreate(activityB)
        manager.requestPermission(callbacks("second"), Permission.RecordAudio)
        assertThat(events).containsExactly("first:denied:Camera")

        activityA.deliver(emptyMap())
        activityA.deliver(mapOf(Permission.Camera.value to true))
        assertThat(events).containsExactly("first:denied:Camera")
        activityB.deliver(mapOf(Permission.RecordAudio.value to true))

        assertThat(events).containsExactly("first:denied:Camera", "second:granted:RecordAudio").inOrder()
    }

    @Test
    fun `Given a request on B and a newer top C, When B is recreated, Then B' keeps B place and delivers`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        val activityC = FakeActivity()
        attachAndCreate(activityC)

        val recreatedB = recreate(activityB)
        assertThat(events).isEmpty()
        recreatedB.deliver(mapOf(Permission.Camera.value to true))
        assertThat(events).containsExactly("first:granted:Camera")
        manager.requestPermission(callbacks("onTop"), Permission.RecordAudio)
        verify(exactly = 1) { activityC.launcher.launch(launched(Permission.RecordAudio)) }
        manager.onDestroy(activityC.activity)
        manager.requestPermission(callbacks("later"), Permission.AccessFineLocation)

        verify(exactly = 1) { recreatedB.launcher.launch(launched(Permission.AccessFineLocation)) }
        verify(exactly = 0) { activityA.launcher.launch(any<Array<String>>()) }
    }

    @Test
    fun `Given B is recreated by a configuration change, When A is destroyed, Then B' is still on top`() {
        attachAndCreate(activityA, activityB)
        val recreatedB = recreate(activityB)

        manager.onDestroy(activityA.activity)
        manager.requestPermission(callbacks(), Permission.Camera)

        verify(exactly = 1) { recreatedB.launcher.launch(launched(Permission.Camera)) }
        verify(exactly = 0) { activityA.launcher.launch(any<Array<String>>()) }
    }

    @Test
    fun `Given two launches on B, When B is recreated, Then B' inherits the launch count`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        manager.requestPermission(callbacks("second"), Permission.Camera)
        events.clear()

        val recreatedB = recreate(activityB)
        recreatedB.deliver(emptyMap())
        assertThat(events).isEmpty()
        recreatedB.deliver(mapOf(Permission.Camera.value to true))

        assertThat(events).containsExactly("second:granted:Camera")
    }

    @Test
    fun `Given a request on B, When another instance with an unknown saved id is created, Then it is denied`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        activityB.configurationChange()
        val other = FakeActivity().apply { restoreFrom(bundleOf(ENTRY_ID to "unknown")) }

        attachAndCreate(other)
        assertThat(events).containsExactly("first:denied:Camera")
        other.deliver(mapOf(Permission.Camera.value to true))
        manager.requestPermission(callbacks("later"), Permission.RecordAudio)

        assertThat(events).containsExactly("first:denied:Camera")
        verify(exactly = 1) { other.launcher.launch(launched(Permission.RecordAudio)) }
    }

    @Test
    fun `Given a request on B, When a fresh instance without saved state is created, Then it is denied and on top`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        activityB.configurationChange()
        val fresh = FakeActivity()

        attachAndCreate(fresh)
        manager.requestPermission(callbacks("later"), Permission.RecordAudio)

        assertThat(events).containsExactly("first:denied:Camera")
        verify(exactly = 1) { fresh.launcher.launch(launched(Permission.RecordAudio)) }
    }

    @Test
    fun `Given a suspended request on B, When an activity that reclaims nothing is created, Then it resumes Denied`() =
        runTest {
            attachAndCreate(activityA, activityB)
            val result = async { manager.requestPermission(Permission.Camera) }
            runCurrent()
            activityB.configurationChange()

            attachAndCreate(FakeActivity())

            assertThat(result.await()).isEqualTo(PermissionResult.Denied(Permission.Camera))
        }

    @Test
    fun `Given two placeholders, When B' then C' are created, Then each reclaims its own entry`() {
        val recreated = twoPlaceholdersRecreated(bFirst = true)

        deliverAndCheckOrder(recreated)
    }

    @Test
    fun `Given two placeholders, When C' then B' are created, Then each reclaims its own entry`() {
        val recreated = twoPlaceholdersRecreated(bFirst = false)

        deliverAndCheckOrder(recreated)
    }

    @Test
    fun `Given isRestored is false, When an activity is created, Then it is new and consumes no state`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        val saved = activityB.configurationChange()
        val recreatedB = FakeActivity().apply { restoreFrom(saved, isRestored = false) }

        attachAndCreate(recreatedB)

        verify(exactly = 0) { recreatedB.savedStateRegistry.consumeRestoredStateForKey(any()) }
        assertThat(events).containsExactly("first:denied:Camera")
    }

    @Test
    fun `Given an attached activity, When it is attached and created, Then the saved state provider holds an id`() {
        attachAndCreate(activityA)
        attachAndCreate(activityA)

        assertThat(activityA.registerCalls).isEqualTo(1)
        assertThat(activityA.providers.keys).containsExactly(SAVED_STATE_KEY)
        assertThat(activityA.saveState().getString(ENTRY_ID)).isNotEmpty()
    }

    @Test
    fun `Given an attached activity, When it is destroyed for a configuration change, Then the provider is removed`() {
        attachAndCreate(activityA)

        activityA.configurationChange()

        assertThat(activityA.providers).isEmpty()
    }

    @Test
    fun `Given an attached activity, When it is destroyed for good, Then the provider is removed`() {
        attachAndCreate(activityA)

        manager.onDestroy(activityA.activity)

        assertThat(activityA.providers).isEmpty()
    }

    @Test
    fun `Given an activity is attached, When onCreate gets a non-attached owner, Then nothing is registered`() {
        attachAndCreate(activityA)

        manager.onCreate(mockk<LifecycleOwner>(relaxed = true))
        manager.requestPermission(callbacks(), Permission.Camera)

        assertThat(activityA.keys).hasSize(1)
        verify(exactly = 1) { activityA.launcher.launch(launched(Permission.Camera)) }
    }

    @Test
    fun `Given nothing attached, When onCreate is called, Then it throws IllegalStateException`() {
        assertThrows(IllegalStateException::class.java) {
            manager.onCreate(mockk<LifecycleOwner>(relaxed = true))
        }
    }

    @Test
    fun `Given a request pending on B, When B is recreated, Then no other entry is denied`() {
        attachAndCreate(activityA, activityB)
        manager.requestPermission(callbacks("first"), Permission.Camera)

        recreate(activityB)

        assertThat(events).isEmpty()
    }

    @Test
    fun `Given the top is attached but not created, When requesting, Then Denied and rationale uses the top`() {
        attachAndCreate(activityA)
        manager.attach(activityB.activity)
        activityA.stubRationale(Permission.Camera, false)

        manager.requestPermission(callbacks(), Permission.Camera)

        assertThat(events).containsExactly("denied:Camera")
        verify(exactly = 0) { activityA.launcher.launch(any<Array<String>>()) }
        activityB.stubRationale(Permission.Camera, true)
        assertThat(manager.shouldShowRationale(Permission.Camera)).isTrue()
    }

    @Test
    fun `Given an activity attached twice, When it is destroyed once, Then hasPermission uses the context`() {
        manager.attach(activityA.activity)
        manager.attach(activityA.activity)
        manager.onCreate(activityA.activity)
        stubGranted(activityA, Permission.Camera)
        assertThat(hasPermission(Permission.Camera)).isTrue()

        manager.onDestroy(activityA.activity)

        assertThat(hasPermission(Permission.Camera)).isFalse()
        manager.requestPermission(callbacks(), Permission.Camera)
        assertThat(events).containsExactly("denied:Camera")
    }

    @Test
    fun `Given an activity attached twice, When it is created, Then it registers a single launcher`() {
        manager.attach(activityA.activity)
        manager.attach(activityA.activity)
        manager.onCreate(activityA.activity)

        manager.requestPermission(callbacks(), Permission.Camera)

        assertThat(activityA.keys).hasSize(1)
        verify(exactly = 1) { activityA.launcher.launch(launched(Permission.Camera)) }
    }

    @Test
    fun `Given several activities with their own registries, When created, Then each key is index 0`() {
        val activityC = FakeActivity()

        attachAndCreate(activityA, activityB, activityC)

        assertThat(activityA.keys).containsExactly("requestPermissions#0")
        assertThat(activityB.keys).containsExactly("requestPermissions#0")
        assertThat(activityC.keys).containsExactly("requestPermissions#0")
    }

    @Test
    fun `Given A and B created and B destroyed, When requestPermission suspend is called, Then A launches it`() =
        runTest {
            attachAndCreate(activityA, activityB)
            manager.onDestroy(activityB.activity)

            val result = async { manager.requestPermission(Permission.Camera) }
            runCurrent()
            verify(exactly = 1) { activityA.launcher.launch(launched(Permission.Camera)) }
            verify(exactly = 0) { activityB.launcher.launch(any<Array<String>>()) }
            activityA.deliver(mapOf(Permission.Camera.value to true))

            assertThat(result.await()).isEqualTo(PermissionResult.Granted(Permission.Camera))
        }

    private fun attachAndCreate(
        vararg fakes: FakeActivity,
    ) {
        fakes.forEach { fake ->
            manager.attach(fake.activity)
            manager.onCreate(fake.activity)
        }
    }

    /** Destroys [fake] for a configuration change and creates its new instance, restoring its saved state. */
    private fun recreate(
        fake: FakeActivity,
    ): FakeActivity {
        val saved = fake.configurationChange()
        return FakeActivity().apply { restoreFrom(saved) }.also { recreated -> attachAndCreate(recreated) }
    }

    private fun FakeActivity.configurationChange(): Bundle {
        val saved = saveState()
        every { activity.isChangingConfigurations } returns true
        manager.onDestroy(activity)
        return saved
    }

    private fun bundleOf(
        entry: Pair<String, String>,
    ) = Bundle().apply { putString(entry.first, entry.second) }

    private fun twoPlaceholdersRecreated(
        bFirst: Boolean,
    ): List<FakeActivity> {
        val activityC = FakeActivity()
        attachAndCreate(activityA, activityB, activityC)
        manager.requestPermission(callbacks("first"), Permission.Camera)
        val savedB = activityB.configurationChange()
        val savedC = activityC.configurationChange()
        val recreatedB = FakeActivity().apply { restoreFrom(savedB) }
        val recreatedC = FakeActivity().apply { restoreFrom(savedC) }
        if (bFirst) attachAndCreate(recreatedB, recreatedC) else attachAndCreate(recreatedC, recreatedB)
        return listOf(recreatedB, recreatedC)
    }

    private fun deliverAndCheckOrder(
        recreated: List<FakeActivity>,
    ) {
        val (recreatedB, recreatedC) = recreated
        assertThat(events).isEmpty()
        recreatedC.deliver(mapOf(Permission.Camera.value to true))
        assertThat(events).containsExactly("first:granted:Camera")
        manager.onDestroy(recreatedC.activity)
        manager.requestPermission(callbacks("later"), Permission.RecordAudio)

        verify(exactly = 1) { recreatedB.launcher.launch(launched(Permission.RecordAudio)) }
        verify(exactly = 0) { activityA.launcher.launch(any<Array<String>>()) }
    }

    private fun hasPermission(
        permission: Permission,
    ): Boolean {
        var granted: Boolean? = null
        manager.hasPermission({ granted = it }, permission)
        return granted == true
    }

    private fun stubGranted(
        fake: FakeActivity,
        permission: Permission,
    ) {
        every {
            ContextCompat.checkSelfPermission(fake.activity, permission.value)
        } returns PackageManager.PERMISSION_GRANTED
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

    /** A mocked activity with its own registry, launcher, saved state registry and result callbacks. */
    private class FakeActivity {

        val activity: ComponentActivity = mockk(relaxed = true)
        val launcher: ActivityResultLauncher<Array<String>> = mockk(relaxed = true)
        val savedStateRegistry: SavedStateRegistry = mockk(relaxed = true)
        val keys = mutableListOf<String>()
        val providers = mutableMapOf<String, SavedStateRegistry.SavedStateProvider>()
        var registerCalls = 0
        private val registry: ActivityResultRegistry = mockk()
        private val resultCallbacks = mutableListOf<ActivityResultCallback<Map<String, Boolean>>>()

        init {
            every { activity.lifecycle } returns mockk<LifecycleRegistry>(relaxed = true)
            every { activity.activityResultRegistry } returns registry
            every { activity.savedStateRegistry } returns savedStateRegistry
            every { savedStateRegistry.isRestored } returns false
            every { savedStateRegistry.consumeRestoredStateForKey(any()) } returns null
            every { savedStateRegistry.registerSavedStateProvider(any(), any()) } answers {
                registerCalls++
                providers[firstArg()] = secondArg()
            }
            every { savedStateRegistry.unregisterSavedStateProvider(any()) } answers { providers.remove(firstArg()) }
            every {
                registry.register(
                    capture(keys),
                    any<LifecycleOwner>(),
                    any<ActivityResultContract<Array<String>, Map<String, Boolean>>>(),
                    capture(resultCallbacks),
                )
            } returns launcher
        }

        fun saveState(): Bundle = providers.getValue(SAVED_STATE_KEY).saveState()

        fun restoreFrom(
            saved: Bundle,
            isRestored: Boolean = true,
        ) {
            every { savedStateRegistry.isRestored } returns isRestored
            every { savedStateRegistry.consumeRestoredStateForKey(SAVED_STATE_KEY) } returns saved
        }

        fun deliver(
            result: Map<String, Boolean>,
        ) {
            resultCallbacks.last().onActivityResult(result)
        }

        fun stubRationale(
            permission: Permission,
            value: Boolean,
        ) {
            every { activity.shouldShowRequestPermissionRationale(permission.value) } returns value
        }
    }

    private companion object {
        const val SAVED_STATE_KEY = "com.raxdenstudios.commons.permissions:requestPermissions#0"
        const val ENTRY_ID = "entryId"
    }
}
