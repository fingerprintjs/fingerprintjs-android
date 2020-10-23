package com.fingerprintjs.android.fingerprint.fingerprinters

import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.hashers.Hasher

class OsBuildFingerprinter(
    private val osBuildInfoProvider: OsBuildInfoProvider,
    private val hasher: Hasher,
    version: Int
) : Fingerprinter(version) {
    override fun calculate(): String {
        val osBuildInfoSB = StringBuilder()
        osBuildInfoSB.append(osBuildInfoProvider.fingerprint())
        return hasher.hash(osBuildInfoSB.toString())
    }
}