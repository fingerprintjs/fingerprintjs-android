package com.fingerprintjs.android.fingerprint.public_api.extended.signals

import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

public sealed class IdentificationSignalV2() {
    public abstract val fingerprintingInfo: FingerprintingInfo
    public abstract fun getHashableString(): String
}

public interface FingerprintingInfo {
    public val addedInVersion: Int
    public val stabilityLevel: StabilityLevel
}
