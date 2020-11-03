package com.fingerprintjs.android.fingerprint.fingerprinters


import com.fingerprintjs.android.fingerprint.datasources.PackageManagerDataSource
import com.fingerprintjs.android.fingerprint.hashers.Hasher


class InstalledAppsFingerprinter(
    private val packageManagerDataSource: PackageManagerDataSource,
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
        val installedAppsSb = StringBuilder()
        packageManagerDataSource
            .getApplicationsList()
            .sortedBy { it.packageName }
            .forEach { installedAppsSb.append(it.packageName) }
        return hasher.hash(installedAppsSb.toString())
    }

}