package com.raxdenstudios.commons.android.audio

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
internal class HeadphonesConnectionObserverImplTest {

    private val context: Context = mockk()
    private val audioManager: AudioManager = mockk(relaxed = true)
    private val callbackSlot = slot<AudioDeviceCallback>()
    private lateinit var observer: HeadphonesConnectionObserverImpl

    @Before
    fun setUp() {
        every { context.getSystemService(AudioManager::class.java) } returns audioManager
        every { audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS) } returns emptyArray()
        every { audioManager.registerAudioDeviceCallback(capture(callbackSlot), any()) } returns Unit
        observer = HeadphonesConnectionObserverImpl(context)
    }

    @Test
    fun `Given a supported sink is added, When observing, Then emit true`() = runTest {
        observer.observeConnectionChanges().test {
            callbackSlot.captured.onAudioDevicesAdded(arrayOf(device(1, AudioDeviceInfo.TYPE_WIRED_HEADSET)))

            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given devices already connected, When the initial callback replays them, Then nothing is emitted`() = runTest {
        val initial = device(1, AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
        every { audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS) } returns arrayOf(initial)

        observer.observeConnectionChanges().test {
            callbackSlot.captured.onAudioDevicesAdded(arrayOf(initial))

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given a connected supported device, When it is removed, Then emit false`() = runTest {
        observer.observeConnectionChanges().test {
            val headset = device(1, AudioDeviceInfo.TYPE_WIRED_HEADPHONES)
            callbackSlot.captured.onAudioDevicesAdded(arrayOf(headset))
            assertThat(awaitItem()).isTrue()

            callbackSlot.captured.onAudioDevicesRemoved(arrayOf(headset))

            assertThat(awaitItem()).isFalse()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given two headphones connected, When one is removed, Then emit true`() = runTest {
        observer.observeConnectionChanges().test {
            val first = device(1, AudioDeviceInfo.TYPE_WIRED_HEADPHONES)
            val second = device(2, AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
            callbackSlot.captured.onAudioDevicesAdded(arrayOf(first))
            assertThat(awaitItem()).isTrue()
            callbackSlot.captured.onAudioDevicesAdded(arrayOf(second))
            assertThat(awaitItem()).isTrue()

            callbackSlot.captured.onAudioDevicesRemoved(arrayOf(first))

            assertThat(awaitItem()).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given two headphones connected, When both are removed one by one, Then the last removal emits false`() =
        runTest {
            observer.observeConnectionChanges().test {
                val first = device(1, AudioDeviceInfo.TYPE_WIRED_HEADPHONES)
                val second = device(2, AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
                callbackSlot.captured.onAudioDevicesAdded(arrayOf(first, second))
                assertThat(awaitItem()).isTrue()

                callbackSlot.captured.onAudioDevicesRemoved(arrayOf(first))
                assertThat(awaitItem()).isTrue()
                callbackSlot.captured.onAudioDevicesRemoved(arrayOf(second))

                assertThat(awaitItem()).isFalse()
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given a connected headphone, When an untracked device is removed, Then nothing is emitted`() = runTest {
        observer.observeConnectionChanges().test {
            callbackSlot.captured.onAudioDevicesAdded(arrayOf(device(1, AudioDeviceInfo.TYPE_WIRED_HEADPHONES)))
            assertThat(awaitItem()).isTrue()

            callbackSlot.captured.onAudioDevicesRemoved(arrayOf(device(99, AudioDeviceInfo.TYPE_WIRED_HEADSET)))

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given an unsupported device type, When it is added or removed, Then nothing is emitted`() = runTest {
        observer.observeConnectionChanges().test {
            val speaker = device(2, AudioDeviceInfo.TYPE_BUILTIN_SPEAKER)

            callbackSlot.captured.onAudioDevicesAdded(arrayOf(speaker))
            callbackSlot.captured.onAudioDevicesRemoved(arrayOf(speaker))

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given a supported type that is not a sink, When it is added, Then nothing is emitted`() = runTest {
        observer.observeConnectionChanges().test {
            callbackSlot.captured.onAudioDevicesAdded(
                arrayOf(device(3, AudioDeviceInfo.TYPE_WIRED_HEADSET, isSink = false)),
            )

            expectNoEvents()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given the collector is cancelled, When observing ends, Then the callback is unregistered`() = runTest {
        observer.observeConnectionChanges().test {
            cancelAndIgnoreRemainingEvents()
        }

        verify { audioManager.unregisterAudioDeviceCallback(callbackSlot.captured) }
    }

    private fun device(
        id: Int,
        type: Int,
        isSink: Boolean = true,
    ): AudioDeviceInfo = mockk {
        every { this@mockk.id } returns id
        every { this@mockk.type } returns type
        every { this@mockk.isSink } returns isSink
    }
}
