package com.fingerprintjs.android.fingerprint.device_id_signals

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.MediaDrmIdProvider

internal class DeviceIdSignalsProvider(
    private val gsfIdProvider: GsfIdProvider,
    private val androidIdProvider: AndroidIdProvider,
    private val mediaDrmIdProvider: MediaDrmIdProvider,
) {

    fun getSignalMatching(version: Fingerprinter.Version): DeviceIdSignal<*> {
        return when(version) {
            in Fingerprinter.Version.V_1..Fingerprinter.Version.V_2 -> {
                gsfIdSignal.takeIf { it.value.isNotEmpty() }
                    ?: androidIdSignal
            }
            else -> {
                gsfIdSignal.takeIf { it.value.isNotEmpty() }
                    ?: mediaDrmIdSignal.takeIf { it.value.isNotEmpty() }
                    ?: androidIdSignal
            }
        }
    }

    val gsfIdSignal: GsfIdSignal by lazy {
        GsfIdSignal(gsfIdProvider.getGsfAndroidId().orEmpty())
    }
    val androidIdSignal: AndroidIdSignal by lazy {
        AndroidIdSignal(androidIdProvider.getAndroidId())
    }
    val mediaDrmIdSignal: MediaDrmIdSignal by lazy {
        MediaDrmIdSignal(mediaDrmIdProvider.getMediaDrmId().orEmpty())
    }
}
