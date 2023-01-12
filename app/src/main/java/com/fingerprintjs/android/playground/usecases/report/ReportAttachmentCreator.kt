package com.fingerprintjs.android.playground.usecases.report

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.BuildConfig
import com.fingerprintjs.android.playground.utils.mappers.description
import com.fingerprintjs.android.playground.utils.getDeviceId
import com.fingerprintjs.android.playground.utils.getFingerprint
import com.fingerprintjs.android.playground.utils.mappers.*
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ReportAttachmentCreator @Inject constructor(
    private val fingerprinter: Fingerprinter,
    private val gson: Gson,
) {
    suspend fun createReportAttachment(): String = withContext(Dispatchers.IO) {
        val reportAttachmentVo = ReportAttachmentVo(
            appVersionName = BuildConfig.VERSION_NAME,
            appVersionCode = BuildConfig.VERSION_CODE,
            fingerprints = createFingerprints(),
            deviceIds = createDeviceIds()
        )
        gson.toJson(reportAttachmentVo)
    }

    private suspend fun createFingerprints(): List<FingerprintInfoVo> {
        return Fingerprinter.Version.values().flatMap { version ->
            StabilityLevel.values().map { value -> version to value }
        }.map { (version, stabilityLevel) ->
            val fingerprint = fingerprinter.getFingerprint(version, stabilityLevel)
            val signals = fingerprinter.getFingerprintingSignalsProvider().getSignalsMatching(
                version, stabilityLevel
            )
            FingerprintInfoVo(version = version.description,
                stabilityLevel = stabilityLevel.description,
                fingerprint = fingerprint,
                signals = signals.map { signal ->
                    FingerprintSignalVo(
                        name = signal.humanName, value = signal.jsonifiableValue
                    )
                })
        }
    }

    private suspend fun createDeviceIds(): List<DeviceIdInfoVo> {
        return Fingerprinter.Version.values().map { version ->
            val deviceId = fingerprinter.getDeviceId(version)
            DeviceIdInfoVo(
                version = version.description, deviceID = deviceId.deviceId, signals = listOf(
                    DeviceIdSignalVo(name = ANDROID_ID_HUMAN_NAME, value = deviceId.androidId),
                    DeviceIdSignalVo(name = GSF_ID_HUMAN_NAME, value = deviceId.gsfId),
                    DeviceIdSignalVo(name = MEDIA_DRM_ID_HUMAN_NAME, value = deviceId.mediaDrmId),
                )
            )
        }
    }
}
