package com.fingerprintjs.android.fingerprint.public_api.signals

import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

// think about keeping generic value in the signal
// pros: sometimes even handling raw value of Any type may make sense. for example, to call JSONObject.wrap(value)
// cons: we will always need to wrap signal's data into some class, which might get cumbersome
public sealed class FingerprintingSignal<T>() {
    public abstract val fingerprintingInfo: FingerprintingInfo
    public abstract val value: T
    public abstract fun getHashableString(): String
}

public sealed interface FingerprintingInfo {
    public val addedInVersion: Int
    public val stabilityLevel: StabilityLevel
}
