package com.fingerprintjs.android.fingerprint.public_api.extended

import com.fingerprintjs.android.fingerprint.public_api.IdentificationVersion
import com.fingerprintjs.android.fingerprint.public_api.extended.signals.IdentificationSignalV2
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher
import com.fingerprintjs.android.fingerprint.tools.hashers.MurMur3x64x128Hasher

public interface IdentificationExtendedApi {
    public fun getDeviceIdProvider(): DeviceIdProvider
    public fun getSignalsProvider(): SignalsProvider

    public fun DeviceIdProvider.getDeviceIdFor(
        version: Int,
    ): String

    public fun SignalsProvider.getSignalsFor(
        version: IdentificationVersion,
        stabilityLevel: StabilityLevel,
    ): List<IdentificationSignalV2>

    public fun List<IdentificationSignalV2>.getFingerprint(
        hasher: Hasher = MurMur3x64x128Hasher()
    ): String
}

