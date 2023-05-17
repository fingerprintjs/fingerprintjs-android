package com.fingerprintjs.android.fingerprint.tools

import com.fingerprintjs.android.fingerprint.Fingerprinter
import com.fingerprintjs.android.fingerprint.fingerprinting_signals.FingerprintingSignal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

internal object SignalsUtils {

    fun <T : FingerprintingSignal<*>> createSignalIfNeeded(
        requiredVersion: Fingerprinter.Version,
        requiredStabilityLevel: StabilityLevel,
        signalFingerprintingInfo: FingerprintingSignal.Info,
        signalFactory: () -> T,
    ): T? {
        return if (
            signalFingerprintingInfo.stabilityLevel.atLeastAsStableAs(requiredStabilityLevel)
            && requiredVersion.inRange(signalFingerprintingInfo.addedInVersion, signalFingerprintingInfo.removedInVersion)
        )
            signalFactory.invoke()
        else
            null
    }
}
