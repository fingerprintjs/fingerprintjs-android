package com.fingerprintjs.android.fingerprint.fingerprinters

class DeviceStateFingerprinter(
    version: Int
) : Fingerprinter(version) {
    override fun calculate(): String {
        return ""
    }
}