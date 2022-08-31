package com.fingerprintjs.android.fingerprint.public_api.extended

import com.fingerprintjs.android.fingerprint.public_api.extended.signals.Test1

public interface SignalsProvider {
    public fun getSignal1(): Test1
    public fun getSignal2(): Test1
}