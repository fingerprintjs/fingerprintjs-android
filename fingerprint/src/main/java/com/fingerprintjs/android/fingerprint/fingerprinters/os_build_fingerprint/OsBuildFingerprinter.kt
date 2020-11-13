package com.fingerprintjs.android.fingerprint.fingerprinters.os_build_fingerprint


import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.fingerprinters.BaseFingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class OsBuildFingerprinter(
    private val osBuildInfoProvider: OsBuildInfoProvider,
    private val hasher: Hasher,
    version: Int
) : BaseFingerprinter<OsBuildRawData>(version) {

    private val rawData = OsBuildRawData(
        osBuildInfoProvider.fingerprint()
    )

    override fun calculate(): String {
        return when (version) {
            1 -> v1()
            else -> v1()
        }
    }

    override fun rawData() = rawData

    private fun v1(): String {
        val osBuildInfoSB = StringBuilder()
        osBuildInfoSB.append(rawData.fingerprint)
        return hasher.hash(osBuildInfoSB.toString())
    }
}