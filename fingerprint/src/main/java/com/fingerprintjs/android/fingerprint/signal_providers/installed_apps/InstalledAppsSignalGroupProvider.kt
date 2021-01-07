package com.fingerprintjs.android.fingerprint.signal_providers.installed_apps


import com.fingerprintjs.android.fingerprint.info_providers.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
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

    override fun fingerprint(): String {
        return when (version) {
            1 -> v1()
            else -> v1()
        }
    }

    override fun rawData() = rawData

    private fun v1(): String {
        val installedAppsSb = StringBuilder()
        rawData
            .applicationsNamesList
            .sortedBy { it.packageName }
            .forEach { installedAppsSb.append(it.packageName) }
        return hasher.hash(installedAppsSb.toString())
    }

}