package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel


interface Fingerprinter {
    fun getDeviceId(listener: (DeviceIdResult) -> (Unit))
    fun getFingerprint(listener: (FingerprintResult) -> (Unit))
    fun getFingerprint(stabilityLevel: StabilityLevel, listener: (FingerprintResult) -> (Unit))

    @Deprecated("Use the method with stability level instead.")
    fun getFingerprint(signalProvidersMask: Int, listener: (FingerprintResult) -> (Unit))
}

interface FingerprintResult {
    val fingerprint: String
    fun <T> getSignalProvider(clazz: Class<T>): T?
}

data class DeviceIdResult(
    val deviceId: String,
    val gsfId: String?,
    val androidId: String
)