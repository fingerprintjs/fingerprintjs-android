package com.fingerprintjs.android.fingerprint.signal_providers.os_build


import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class OsBuildSignalProvider(
    private val osBuildInfoProvider: OsBuildInfoProvider,
    private val hasher: Hasher,
    version: Int
) : SignalProvider<OsBuildRawData>(version) {

    private val rawData =
        OsBuildRawData(
            osBuildInfoProvider.fingerprint()
        )

    override fun fingerprint(): String {
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