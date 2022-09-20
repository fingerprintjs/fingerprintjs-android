package com.fingerprintjs.android.fingerprint.public_api

import com.fingerprintjs.android.fingerprint.IdentificationApiFactoryImpl
import com.fingerprintjs.android.fingerprint.public_api.signals.FingerprintingSignal
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher

// maybe make it class? as well as other classes
public abstract class IdentificationApi internal constructor() {
    // interop with java?
    // maybe also provide async api? but we won't want to do so inside interface
    // but maybe using class in enough for this thing
    public abstract fun getFingerprint(
        @IdentificationVersionRange(from = IdentificationVersion.V_5) version: IdentificationVersion,
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
        hasher: Hasher = MurMur3x64x128Hasher(),
    )

    public abstract fun getFingerprint(
        signals: List<FingerprintingSignal>,
        hasher: Hasher = MurMur3x64x128Hasher(),
    )

    public abstract fun getFingerprintAsync(
        @IdentificationVersionRange(from = IdentificationVersion.V_5) version: IdentificationVersion,
        stabilityLevel: StabilityLevel = StabilityLevel.OPTIMAL,
        hasher: Hasher = MurMur3x64x128Hasher(),
        listener: (String) -> Unit,
    )

    public abstract fun getFingerprintAsync(
        signals: List<FingerprintingSignal>,
        hasher: Hasher = MurMur3x64x128Hasher(),
        listener: (String) -> Unit,
    )

    // same concerns
    public fun getDeviceId(
        @IdentificationVersionRange(from = IdentificationVersion.V_1) version: IdentificationVersion
    ): String {
        return getDeviceIdProvider().getDeviceIdMatching(version)
    }

    public abstract fun getDeviceIdAsync(
        @IdentificationVersionRange(from = IdentificationVersion.V_1) version: IdentificationVersion,
        listener: (String) -> Unit,
    )

    public abstract fun getDeviceIdProvider(): DeviceIdProvider
    public abstract fun getFingerprintingSignalsProvider(): FingerprintingSignalsProvider

    public companion object {
        public fun Factory(): IdentificationApiFactory = IdentificationApiFactoryImpl()
    }
}
