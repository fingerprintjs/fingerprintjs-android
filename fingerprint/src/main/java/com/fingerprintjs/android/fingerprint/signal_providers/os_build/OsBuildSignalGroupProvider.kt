package com.fingerprintjs.android.fingerprint.signal_providers.os_build


import com.fingerprintjs.android.fingerprint.info_providers.CodecInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.DeviceSecurityInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.OsBuildInfoProvider
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class OsBuildSignalGroupProvider(
    osBuildInfoProvider: OsBuildInfoProvider,
    codecInfoProvider: CodecInfoProvider?,
    deviceSecurityInfoProvider: DeviceSecurityInfoProvider,
    private val hasher: Hasher,
    version: Int
) : SignalGroupProvider<OsBuildRawData>(version) {

    private val rawData =
        OsBuildRawData(
            osBuildInfoProvider.fingerprint(),
            osBuildInfoProvider.androidVersion(),
            osBuildInfoProvider.sdkVersion(),
            osBuildInfoProvider.kernelVersion(),
            codecInfoProvider?.codecsList() ?: emptyList(),
            deviceSecurityInfoProvider.encryptionStatus(),
            deviceSecurityInfoProvider.securityProvidersData()
        )

    override fun fingerprint(stabilityLevel: StabilityLevel): String {
        return hasher.hash(
            combineSignals(
                when (version) {
                    1 -> v1()
                    2 -> v2()
                    else -> v2()
                }, stabilityLevel
            )
        )

    }

    override fun rawData() = rawData

    private fun v1() = listOf(
        rawData.fingerprint()
    )

    private fun v2() = listOf(
        rawData.androidVersion(),
        rawData.sdkVersion(),
        rawData.kernelVersion(),
        rawData().encryptionStatus(),
        rawData().securityProviders(),
        rawData().codecList()
    )
}