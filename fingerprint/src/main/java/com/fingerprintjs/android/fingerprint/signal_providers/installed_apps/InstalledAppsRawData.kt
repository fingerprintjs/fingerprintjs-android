package com.fingerprintjs.android.fingerprint.signal_providers.installed_apps

import com.fingerprintjs.android.fingerprint.datasources.PackageInfo

data class InstalledAppsRawData(
    val applicationsNamesList: List<PackageInfo>
)