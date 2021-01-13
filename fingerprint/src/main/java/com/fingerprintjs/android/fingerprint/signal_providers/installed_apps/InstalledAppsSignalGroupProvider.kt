package com.fingerprintjs.android.fingerprint.signal_providers.installed_apps


import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class InstalledAppsSignalGroupProvider(
    packageManagerDataSource: PackageManagerDataSource,
    private val hasher: Hasher,
    version: Int
) : SignalGroupProvider<InstalledAppsRawData>(version) {

    private val rawData =
        InstalledAppsRawData(
            packageManagerDataSource.getApplicationsList(),
            packageManagerDataSource.getSystemApplicationsList()
        )

    override fun fingerprint(stabilityLevel: StabilityLevel): String {
        return hasher.hash(
            combineSignals(
                when (version) {
                    1 -> v1()
                    2 -> v2()
                    else -> v1()
                }, stabilityLevel
            )
        )
    }

    override fun rawData() = rawData

    private fun v1() = listOf(
        rawData.applicationsList()
    )

    private fun v2() = listOf(
        rawData.applicationsList(),
        rawData.systemApplicationsList()
    )
}