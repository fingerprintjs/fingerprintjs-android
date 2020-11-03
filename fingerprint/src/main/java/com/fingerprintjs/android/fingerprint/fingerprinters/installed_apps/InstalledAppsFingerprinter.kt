package com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps


import com.fingerprintjs.android.fingerprint.datasources.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.fingerprinters.Fingerprinter
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class InstalledAppsFingerprinter(
    packageManagerDataSource: PackageManagerDataSource,
    private val hasher: Hasher,
    version: Int
) : Fingerprinter<InstalledAppsRawData>(version) {

    private val rawData = InstalledAppsRawData(
        packageManagerDataSource.getApplicationsList()
    )

    override fun calculate(): String {
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