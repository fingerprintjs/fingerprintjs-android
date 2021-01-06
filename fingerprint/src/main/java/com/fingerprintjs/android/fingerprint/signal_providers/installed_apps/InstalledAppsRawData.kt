package com.fingerprintjs.android.fingerprint.signal_providers.installed_apps

import com.fingerprintjs.android.fingerprint.info_providers.PackageInfo

data class InstalledAppsRawData(
    val applicationsNamesList: List<PackageInfo>
)