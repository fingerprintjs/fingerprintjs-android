package com.fingerprintjs.android.playground.ui.screens.home.fingerprint

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

data class FingerprintScreenState(
    val stabilityLevel: StabilityLevel,
    val version: Fingerprinter.Version,
    val fingerprintInfo: FingerprintInfoState,
) {
    sealed class FingerprintInfoState() {
        object Initializing : FingerprintInfoState()
        class Ready(
            val fingerprint: String,
            val signals: List<FingerprintItemData>,
        ) : FingerprintInfoState()
    }
}