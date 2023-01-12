package com.fingerprintjs.android.playground.ui.screens.home

import android.annotation.SuppressLint
import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.FingerprintingSignal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.ui.screens.home.device_id.DeviceIdScreenState
import com.fingerprintjs.android.playground.ui.screens.home.device_id.DeviceIdSignalItemData
import com.fingerprintjs.android.playground.ui.screens.home.fingerprint.FingerprintItemData
import com.fingerprintjs.android.playground.ui.screens.home.fingerprint.FingerprintScreenState
import com.fingerprintjs.android.playground.utils.mappers.*

fun createDeviceIdInfoReadyState(deviceIdResult: DeviceIdResult): DeviceIdScreenState.DeviceIdInfoState.Ready {
    return DeviceIdScreenState.DeviceIdInfoState.Ready(
        deviceId = deviceIdResult.deviceIdPrettified,
        signals = listOf(
            DeviceIdSignalItemData(
                signalName = ANDROID_ID_HUMAN_NAME,
                signalValue = deviceIdResult.androidIdPrettified
            ),
            DeviceIdSignalItemData(
                signalName = GSF_ID_HUMAN_NAME,
                signalValue = deviceIdResult.gsfIdPrettified
            ),
            DeviceIdSignalItemData(
                signalName = MEDIA_DRM_ID_HUMAN_NAME,
                signalValue = deviceIdResult.mediaDrmIdPrettified
            ),
        )
    )
}

fun createFingerprintInfoReadyState(
    fingerprint: String,
    signals: List<FingerprintingSignal<*>>,
): FingerprintScreenState.FingerprintInfoState.Ready {
    return FingerprintScreenState.FingerprintInfoState.Ready(
        fingerprint = fingerprint,
        signals = signals.mapIndexed { _, signal -> signal.toFingerprintItemData() }
    )
}

@SuppressLint("DiscouragedApi")
fun createDeviceIdScreenInitialState() = DeviceIdScreenState(
    version = Fingerprinter.Version.latest,
    deviceIdInfo = DeviceIdScreenState.DeviceIdInfoState.Initializing
)

@SuppressLint("DiscouragedApi")
fun createFingerprintScreenInitialState() = FingerprintScreenState(
    stabilityLevel = StabilityLevel.STABLE,
    version = Fingerprinter.Version.latest,
    fingerprintInfo = FingerprintScreenState.FingerprintInfoState.Initializing,
)

@SuppressLint("DiscouragedApi")
private fun FingerprintingSignal<*>.toFingerprintItemData(): FingerprintItemData {
    return FingerprintItemData(
        signalName = this.humanName,
        signalValue = this.humanValue,
        stabilityLevel = this.info.stabilityLevel,
        versionStart = this.info.addedInVersion,
        versionEnd = this.info.removedInVersion ?: Fingerprinter.Version.latest,
    )
}
