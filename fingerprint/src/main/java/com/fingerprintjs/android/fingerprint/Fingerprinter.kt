package com.fingerprintjs.android.fingerprint


interface Fingerprinter {
    fun getDeviceId(listener: (DeviceIdResult) -> (Unit))
    fun getFingerprint(listener: (FingerprintResult) -> (Unit))
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