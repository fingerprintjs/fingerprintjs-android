package com.fingerprintjs.android.fingerprint.fingerprinters.installed_apps

import com.fingerprintjs.android.fingerprint.datasources.PackageInfo

data class InstalledAppsRawData(
    val applicationsNamesList: List<PackageInfo>
)