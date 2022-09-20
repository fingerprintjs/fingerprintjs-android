package com.fingerprintjs.android.fingerprint.public_api

import com.fingerprintjs.android.fingerprint.IdentificationApiFactoryImpl
import com.fingerprintjs.android.fingerprint.public_api.extended.DeviceIdProvider
import com.fingerprintjs.android.fingerprint.public_api.extended.FingerprintingSignalsProvider
import com.fingerprintjs.android.fingerprint.public_api.extended.signals.FingerprintingSignal
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
        return getFingerprint(
            signals = getSignalsProvider().getSignalsMatching(version, stabilityLevel)
        )
    }

    public fun getFingerprint(
        signals: List<FingerprintingSignal>,
    ): String {
        return TODO()
    }

    // same concerns
    public fun getDeviceId(
        @IdentificationVersionRange(from = IdentificationVersion.V_1) version: IdentificationVersion
    ): String {
        return getDeviceIdProvider().getDeviceIdMatching(version)
    }

    public abstract fun List<FingerprintingSignal>.getFingerprint(
        hasher: Hasher = MurMur3x64x128Hasher()
    ): String

    public abstract fun getDeviceIdProvider(): DeviceIdProvider
    public abstract fun getSignalsProvider(): FingerprintingSignalsProvider

    public companion object {
        public fun Factory(): IdentificationApiFactory = IdentificationApiFactoryImpl()
    }
}
