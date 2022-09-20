package com.fingerprintjs.android.fingerprint.public_api.signals

import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

public class Test1 : FingerprintingSignal() {
    override val fingerprintingInfo: FingerprintingInfo
        get() = Companion

    override fun getHashableString(): String {
        TODO("Not yet implemented")
    }

    public companion object : FingerprintingInfo {
        override val addedInVersion: Int
            get() = TODO("Not yet implemented")
        override val stabilityLevel: StabilityLevel
            get() = TODO("Not yet implemented")
    }
}
