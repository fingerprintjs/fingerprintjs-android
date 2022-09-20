package com.fingerprintjs.android.fingerprint.public_api

import com.fingerprintjs.android.fingerprint.public_api.extended.IdentificationExtendedApi
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher

// maybe make it class? as well as other classes
public abstract class IdentificationApi internal constructor() {
    // interop with java?
    // maybe also provide async api? but we won't want to do so inside interface
    // but maybe using class in enough for this thing
    public fun getFingerprint(
        @IdentificationVersionRange(from = IdentificationVersion.V_5) version: IdentificationVersion,
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
        hasher: Hasher = MurMur3x64x128Hasher(),
    ): String {
        return with(getExtendedApi()) {
            getSignalsProvider()
                .getSpecificSignals(version, stabilityLevel)
                .getFingerprint(hasher)
        }
    }

    // same concerns
    public fun getDeviceId(
        @IdentificationVersionRange(from = IdentificationVersion.V_5) version: Int
    ): String {
        return with(getExtendedApi()) {
            getDeviceIdProvider()
                .getDeviceId(version)
        }
    }

    public abstract fun getExtendedApi(): IdentificationExtendedApi
}

