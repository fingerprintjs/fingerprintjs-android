package com.fingerprintjs.android.fingerprint.signal_providers.device_id


import com.fingerprintjs.android.fingerprint.device_id_providers.AndroidIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.GsfIdProvider
import com.fingerprintjs.android.fingerprint.device_id_providers.MediaDrmIdProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public class DeviceIdProvider(
    gsfIdProvider: GsfIdProvider,
    androidIdProvider: AndroidIdProvider,
    mediaDrmIdProvider: MediaDrmIdProvider,
    version: Int
) : SignalGroupProvider<DeviceIdRawData>(version) {

    private val rawData by lazy {
        DeviceIdRawData(
            androidId = androidIdProvider.getAndroidId(),
            gsfId = gsfIdProvider.getGsfAndroidId(),
            mediaDrmId = mediaDrmIdProvider.getMediaDrmId()
        )
    }

    override fun rawData(): DeviceIdRawData = rawData

    override fun fingerprint(stabilityLevel: StabilityLevel): String = when (version) {
        1 -> v1()
        2 -> v1()
        3 -> v3()
        else -> v3()
    }

    private fun v1(): String {
        return if (rawData.gsfId().value.isEmpty()) {
            rawData.androidId()
        } else {
            rawData.gsfId()
        }.value
    }

    private fun v3(): String {
        val gsfId = if (rawData.gsfId().value.isEmpty()) {
            null
        } else {
            rawData.gsfId().value
        }

        val mediaDrmId = if (rawData.mediaDrmId().value.isEmpty()) {
            null
        } else {
            rawData.mediaDrmId().value
        }

        return gsfId ?: mediaDrmId ?: rawData.androidId().value
    }
}