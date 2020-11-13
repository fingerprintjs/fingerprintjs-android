package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.tools.hashers.HasherType


data class Configuration @JvmOverloads constructor(
    val hardwareFingerprintVersion: Int = HARDWARE_FINGERPRINTER_DEFAULT_VERSION,
    val osBuildFingerprintVersion: Int = OS_BUILD_FINGERPRINTER_DEFAULT_VERSION,
    val deviceStateFingerprintVersion: Int = DEVICE_STATE_FINGERPRINTER_DEFAULT_VERSION,
    val installedAppsFingerprintVersion: Int = INSTALLED_APPS_FINGERPRINTER_DEFAULT_VERSION,
    val hasherType: HasherType = HasherType.MurMur3
)

private const val HARDWARE_FINGERPRINTER_DEFAULT_VERSION = 1
private const val OS_BUILD_FINGERPRINTER_DEFAULT_VERSION = 1
private const val DEVICE_STATE_FINGERPRINTER_DEFAULT_VERSION = 1
private const val INSTALLED_APPS_FINGERPRINTER_DEFAULT_VERSION = 1