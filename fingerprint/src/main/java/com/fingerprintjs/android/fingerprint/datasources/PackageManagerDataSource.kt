package com.fingerprintjs.android.fingerprint.datasources

import android.content.pm.PackageManager
import com.fingerprintjs.android.fingerprint.tools.executeSafe


interface PackageManagerDataSource {
    fun getApplicationsList(): List<PackageInfo>
}

data class PackageInfo(
    val packageName: String
)

class PackageManagerDataSourceImpl(
    private val packageManager: PackageManager
) : PackageManagerDataSource {
    override fun getApplicationsList(): List<PackageInfo> {
        return executeSafe(
            {
                packageManager
                    .getInstalledApplications(PackageManager.GET_META_DATA)
                    .map {
                        PackageInfo(
                            it.packageName
                        )
                    }
            }, emptyList()
        )
    }
}
