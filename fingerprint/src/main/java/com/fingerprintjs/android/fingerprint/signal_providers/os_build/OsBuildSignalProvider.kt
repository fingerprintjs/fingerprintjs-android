package com.fingerprintjs.android.fingerprint.signal_providers.os_build


import com.fingerprintjs.android.fingerprint.datasources.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class OsBuildSignalProvider(
        osBuildInfoProvider: OsBuildInfoProvider,
        private val hasher: Hasher,
        version: Int
) : SignalProvider<OsBuildRawData>(version) {

    private val rawData =
            OsBuildRawData(
                    osBuildInfoProvider.fingerprint(),
                    osBuildInfoProvider.androidVersion(),
                    osBuildInfoProvider.sdkVersion(),
                    osBuildInfoProvider.kernelVersion()
            )

    override fun fingerprint(): String {
        return hasher.hash(when (version) {
            1 -> v1()
            2 -> v2()
            else -> v2()
        })

    }

    override fun rawData() = rawData

    private fun v1(): String {
        val osBuildInfoSB = StringBuilder()

        osBuildInfoSB.append(rawData.fingerprint)

        return osBuildInfoSB.toString()
    }

    private fun v2(): String {
        val osBuildInfoSB = StringBuilder()

        osBuildInfoSB.append(rawData.androidVersion)
        osBuildInfoSB.append(rawData.sdkVersion)
        osBuildInfoSB.append(rawData.kernelVersion)

        return osBuildInfoSB.toString()
    }
}