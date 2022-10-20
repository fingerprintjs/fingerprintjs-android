package com.fingerprintjs.android.fingerprint.signal_providers.installed_apps


import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public class InstalledAppsSignalGroupProvider(
    packageManagerDataSource: PackageManagerDataSource,
    private val hasher: Hasher,
    version: Int
) : SignalGroupProvider<InstalledAppsRawData>(version) {

    private val rawData by lazy {
        InstalledAppsRawData(
            packageManagerDataSource.getApplicationsList(),
            packageManagerDataSource.getSystemApplicationsList()
        )
    }

    override fun fingerprint(stabilityLevel: StabilityLevel): String {
        return hasher.hash(
            when (version) {
                1 -> combineSignals(v1(), stabilityLevel = StabilityLevel.UNIQUE)
                2 -> combineSignals(v2(), stabilityLevel)
                else -> combineSignals(v2(), stabilityLevel)
            }
        )
    }

    override fun rawData(): InstalledAppsRawData = rawData

    private fun v1() = listOf(
        rawData.applicationsList()
    )

    private fun v2() = listOf(
        rawData.applicationsList(),
        rawData.systemApplicationsList()
    )
}