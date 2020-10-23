package com.fingerprintjs.android.fingerprint


interface FingerprintAndroidAgent {
    fun deviceId(): String
    fun getFingerprint(flags: Int = (HARDWARE or OS_BUILD or DEVICE_STATE))

    fun hardwareFingerprint(): String
    fun osBuildFingerprint(): String
    fun installedAppsFingerprint(): String
    fun deviceStateFingerprint(): String

    companion object FingerprintType {
        @JvmField
        val HARDWARE = 1
        @JvmField
        val OS_BUILD = 1 shl 1
        @JvmField
        val INSTALLED_APPS = 1 shl 2
        @JvmField
        val DEVICE_STATE = 1 shl 3
    }
}



