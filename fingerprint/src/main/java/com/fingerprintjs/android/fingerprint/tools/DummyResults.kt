package com.fingerprintjs.android.fingerprint.tools

import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider

internal object DummyResults {
    private const val dummyString = ""
    const val fingerprint = dummyString
    val fingerprintResult = object : FingerprintResult {
        override val fingerprint: String
            get() = this@DummyResults.fingerprint

        override fun <T : SignalGroupProvider<*>> getSignalProvider(clazz: Class<T>): T? {
            return null
        }
    }
    val deviceIdResult = DeviceIdResult(
        deviceId = dummyString,
        gsfId = dummyString,
        androidId = dummyString,
        mediaDrmId = dummyString,
    )
}
