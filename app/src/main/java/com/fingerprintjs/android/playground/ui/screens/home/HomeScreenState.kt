package com.fingerprintjs.android.playground.ui.screens.home

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.playground.ui.screens.home.device_id.DeviceIdScreenState
import com.fingerprintjs.android.playground.ui.screens.home.device_id.DeviceIdSignalItemData
import com.fingerprintjs.android.playground.ui.screens.home.fingerprint.FingerprintItemData
import com.fingerprintjs.android.playground.ui.screens.home.fingerprint.FingerprintScreenState
import com.fingerprintjs.android.playground.utils.FP_SIGNALS_PREVIEW_SHORT_VALUE
import com.fingerprintjs.android.playground.utils.FP_SIGNALS_PREVIEW_LONG_VALUE

data class HomeScreenState(
    val fingerprintScreenState: FingerprintScreenState,
    val deviceIdScreenState: DeviceIdScreenState,
) {
    companion object {
        val Preview = HomeScreenState(
            fingerprintScreenState = FingerprintScreenState(
                stabilityLevel = StabilityLevel.STABLE,
                version = Fingerprinter.Version.V_5,
                fingerprintInfo = FingerprintScreenState.FingerprintInfoState.Ready(
                    fingerprint = "asdlfkkl42345sfg",
                    signals = listOf(
                        FingerprintItemData(
                            signalName = "CPU Info V2",
                            signalValue = FP_SIGNALS_PREVIEW_LONG_VALUE,
                            stabilityLevel = StabilityLevel.STABLE,
                            versionStart = Fingerprinter.Version.V_4,
                            versionEnd = Fingerprinter.Version.V_5,
                        ),
                        FingerprintItemData(
                            signalName = "CPU Info V2",
                            signalValue = FP_SIGNALS_PREVIEW_SHORT_VALUE,
                            stabilityLevel = StabilityLevel.STABLE,
                            versionStart = Fingerprinter.Version.V_4,
                            versionEnd = Fingerprinter.Version.V_5,
                        ),
                        FingerprintItemData(
                            signalName = "CPU Info V2",
                            signalValue = FP_SIGNALS_PREVIEW_LONG_VALUE,
                            stabilityLevel = StabilityLevel.STABLE,
                            versionStart = Fingerprinter.Version.V_4,
                            versionEnd = Fingerprinter.Version.V_5,
                        ),
                        FingerprintItemData(
                            signalName = "CPU Info V2",
                            signalValue = FP_SIGNALS_PREVIEW_LONG_VALUE,
                            stabilityLevel = StabilityLevel.STABLE,
                            versionStart = Fingerprinter.Version.V_4,
                            versionEnd = Fingerprinter.Version.V_5,
                        ),
                        FingerprintItemData(
                            signalName = "CPU Info V2",
                            signalValue = FP_SIGNALS_PREVIEW_LONG_VALUE,
                            stabilityLevel = StabilityLevel.STABLE,
                            versionStart = Fingerprinter.Version.V_4,
                            versionEnd = Fingerprinter.Version.V_5,
                        ),
                        FingerprintItemData(
                            signalName = "CPU Info V2",
                            signalValue = FP_SIGNALS_PREVIEW_SHORT_VALUE,
                            stabilityLevel = StabilityLevel.STABLE,
                            versionStart = Fingerprinter.Version.V_4,
                            versionEnd = Fingerprinter.Version.V_5,
                        ),
                    )
                )
            ),
            deviceIdScreenState = DeviceIdScreenState(
                version = Fingerprinter.Version.V_5,
                deviceIdInfo = DeviceIdScreenState.DeviceIdInfoState.Ready(
                    deviceId = "asdklfj452345adsf",
                    signals = listOf(
                        DeviceIdSignalItemData(
                            signalName = "Android ID",
                            signalValue = "asdklfj452345adsf",
                        ),
                        DeviceIdSignalItemData(
                            signalName = "Android ID",
                            signalValue = "asdklfj452345adsf",
                        ),
                        DeviceIdSignalItemData(
                            signalName = "Android ID",
                            signalValue = "asdklfj452345adsf",
                        ),
                        DeviceIdSignalItemData(
                            signalName = "Android ID",
                            signalValue = "asdklfj452345adsf",
                        ),
                        DeviceIdSignalItemData(
                            signalName = "Android ID",
                            signalValue = "asdklfj452345adsf",
                        ),
                    )
                )
            )
        )
    }
}