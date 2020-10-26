package com.fingerprintjs.android.fingerprint.fingerprinters


import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.hashers.Hasher


class OsBuildFingerprinter(
    private val osBuildInfoProvider: OsBuildInfoProvider,
    private val hasher: Hasher,
    version: Int
) : Fingerprinter(version) {

    override fun calculate(): String {
        return when (version) {
            1 -> v1()
            else -> v1()
        }
    }

    private fun v1(): String {
        val osBuildInfoSB = StringBuilder()
        osBuildInfoSB.append(osBuildInfoProvider.fingerprint())
        return hasher.hash(osBuildInfoSB.toString())
    }
}