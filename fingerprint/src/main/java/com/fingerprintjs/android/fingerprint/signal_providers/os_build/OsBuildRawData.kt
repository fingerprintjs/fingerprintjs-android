package com.fingerprintjs.android.fingerprint.signal_providers.os_build


import com.fingerprintjs.android.fingerprint.info_providers.MediaCodecInfo


data class OsBuildRawData(
    val fingerprint: String,
    val androidVersion: String,
    val sdkVersion: String,
    val kernelVersion: String,
    val codecList: List<MediaCodecInfo>,
    val encryptionStatus: String,
    val securityProvidersData: List<Pair<String, String>>
)