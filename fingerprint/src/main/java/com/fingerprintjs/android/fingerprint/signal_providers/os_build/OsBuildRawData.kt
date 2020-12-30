package com.fingerprintjs.android.fingerprint.signal_providers.os_build


data class OsBuildRawData(
        val fingerprint: String,
        val androidVersion: String,
        val sdkVersion: String,
        val kernelVersion: String
)