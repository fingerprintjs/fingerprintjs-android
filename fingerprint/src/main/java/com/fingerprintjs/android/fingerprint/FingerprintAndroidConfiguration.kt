package com.fingerprintjs.android.fingerprint


import com.fingerprintjs.android.fingerprint.tools.hashers.HasherType


data class FingerprintAndroidConfiguration(
    val hardwareFingerprintVersion: Int,
    val osBuildFingerprintVersion: Int,
    val deviceStateFingerprintVersion: Int,
    val installedAppsFingerprintVersion: Int,
    val hasherType: HasherType
)