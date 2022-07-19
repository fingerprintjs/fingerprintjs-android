package com.fingerprintjs.android.fingerprint

import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel

interface Fingerprinter {
    fun getDeviceId(listener: (DeviceIdResult) -> (Unit))
    fun getFingerprint(listener: (FingerprintResult) -> (Unit))
    fun getFingerprint(stabilityLevel: StabilityLevel, listener: (FingerprintResult) -> (Unit))
}

interface FingerprintResult {
    val fingerprint: String
    fun <T : SignalGroupProvider<*>> getSignalProvider(clazz: Class<T>): T?
}

data class DeviceIdResult(
    val deviceId: String,
    val gsfId: String?,
    val androidId: String,
    val mediaDrmId: String?
)