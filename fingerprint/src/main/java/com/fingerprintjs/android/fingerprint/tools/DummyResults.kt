package com.fingerprintjs.android.fingerprint.tools

import com.fingerprintjs.android.fingerprint.DeviceIdResult
import com.fingerprintjs.android.fingerprint.FingerprintResult
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider

internal object DummyResults {
    private const val ALL_ZEROS = "0000000000000000"
    const val fingerprint = ALL_ZEROS
    val fingerprintResult = object : FingerprintResult {
        override val fingerprint: String
            get() = this@DummyResults.fingerprint

        override fun <T : SignalGroupProvider<*>> getSignalProvider(clazz: Class<T>): T? {
            return null
        }
    }
    val deviceIdResult = DeviceIdResult(
        deviceId = ALL_ZEROS,
        gsfId = ALL_ZEROS,
        androidId = ALL_ZEROS,
        mediaDrmId = ALL_ZEROS,
    )
}
