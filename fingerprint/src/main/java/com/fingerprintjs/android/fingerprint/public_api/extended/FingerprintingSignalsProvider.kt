package com.fingerprintjs.android.fingerprint.public_api.extended

import com.fingerprintjs.android.fingerprint.public_api.IdentificationVersion
import com.fingerprintjs.android.fingerprint.public_api.IdentificationVersionRange
import com.fingerprintjs.android.fingerprint.public_api.extended.signals.FingerprintingSignal
import com.fingerprintjs.android.fingerprint.public_api.extended.signals.Test1
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

public abstract class FingerprintingSignalsProvider internal constructor() {
    /**
     * Shorthand method returning the subset of signals listed below in this class
     * with respect to provided parameters.
     */
    public abstract fun getSignalsMatching(
        @IdentificationVersionRange(from = IdentificationVersion.V_5) version: IdentificationVersion,
        stabilityLevel: StabilityLevel,
    ): List<FingerprintingSignal>

    // All signals:

    public abstract fun getSignal1(): Test1
    public abstract fun getSignal2(): Test1
}