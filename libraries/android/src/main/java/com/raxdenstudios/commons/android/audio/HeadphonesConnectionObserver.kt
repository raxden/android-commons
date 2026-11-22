package com.raxdenstudios.commons.android.audio

import android.content.Context
import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import android.media.AudioManager
import android.os.Build
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Detects headphone-like outputs (wired, USB, Bluetooth A2DP/LE, hearing aids) connecting and disconnecting.
 *
 * ## Setup
 *
 * Use the default implementation with an application or activity context; no permission is needed.
 *
 * ## Usage
 *
 * ```kotlin
 * val observer: HeadphonesConnectionObserver = HeadphonesConnectionObserverImpl(applicationContext)
 * lifecycleScope.launch {
 *     observer.observeConnectionChanges().collect { connected -> updateAudioRoute(connected) }
 * }
 * ```
 */
interface HeadphonesConnectionObserver {

    /**
     * Emits `true` when a supported headphone-like output is added. When a connected supported output is
     * removed, emits `true` if another supported output is still connected and `false` if none is left.
     * Only changes are emitted: the devices already connected when
     * collection starts produce no initial value.
     *
     * The flow is cold: every collector registers its own audio device callback, which is removed when
     * the collection is cancelled. Outputs of other types (built-in speaker, HDMI, and so on) are ignored.
     */
    fun observeConnectionChanges(): Flow<Boolean>
}

/** Default [HeadphonesConnectionObserver] backed by the system [AudioManager] of [context]. */
class HeadphonesConnectionObserverImpl(
    private val context: Context,
) : HeadphonesConnectionObserver {

    override fun observeConnectionChanges(): Flow<Boolean> = callbackFlow {
        val audioManager = context.getSystemService(AudioManager::class.java)
        // Registering replays the current devices through onAudioDevicesAdded; seeding the set
        // first makes those initial callbacks a no-op.
        val connectedIds = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
            .supportedOutputIds()
            .toMutableSet()

        val callback = object : AudioDeviceCallback() {
            override fun onAudioDevicesAdded(
                addedDevices: Array<out AudioDeviceInfo>,
            ) {
                val newIds = addedDevices.supportedOutputIds().filter(connectedIds::add)
                if (newIds.isNotEmpty()) trySend(true)
            }

            override fun onAudioDevicesRemoved(
                removedDevices: Array<out AudioDeviceInfo>,
            ) {
                // Only report the remaining state: another headphone may still be connected.
                val removed = connectedIds.removeAll(removedDevices.map { it.id }.toSet())
                if (removed) trySend(connectedIds.isNotEmpty())
            }
        }

        audioManager.registerAudioDeviceCallback(callback, null)
        awaitClose { audioManager.unregisterAudioDeviceCallback(callback) }
    }

    private fun Array<out AudioDeviceInfo>.supportedOutputIds(): List<Int> =
        filter { it.isSink && it.type in SUPPORTED_TYPES }.map { it.id }

    private companion object {
        val SUPPORTED_TYPES = buildSet {
            add(AudioDeviceInfo.TYPE_WIRED_HEADSET)
            add(AudioDeviceInfo.TYPE_WIRED_HEADPHONES)
            add(AudioDeviceInfo.TYPE_USB_HEADSET)
            add(AudioDeviceInfo.TYPE_USB_DEVICE)
            add(AudioDeviceInfo.TYPE_BLUETOOTH_A2DP)
            add(AudioDeviceInfo.TYPE_HEARING_AID)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                add(AudioDeviceInfo.TYPE_BLE_HEADSET)
                add(AudioDeviceInfo.TYPE_BLE_SPEAKER)
            }
        }
    }
}
