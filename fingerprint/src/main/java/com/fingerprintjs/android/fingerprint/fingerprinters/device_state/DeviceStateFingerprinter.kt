package com.fingerprintjs.android.fingerprint.fingerprinters


import com.fingerprintjs.android.fingerprint.datasources.DevicePersonalizationDataSource
import com.fingerprintjs.android.fingerprint.datasources.SettingsDataSource
import com.fingerprintjs.android.fingerprint.hashers.Hasher


class DeviceStateFingerprinter(
    private val settingsDataSource: SettingsDataSource,
    private val devicePersonalizationDataSource: DevicePersonalizationDataSource,
    private val hasher: Hasher,
    version: Int
) : Fingerprinter(version) {
    override fun calculate(): String {
        return when(version) {
            1 -> v1()
            else -> v1()
        }
    }

    private fun v1(): String {
        val deviceStateSb = StringBuilder()
        deviceStateSb
            .append(settingsDataSource.accelerometerRotationEnabled())
            .append(settingsDataSource.accessibilityEnabled())
            .append(settingsDataSource.alarmAlertPath())
            .append(settingsDataSource.dataRoamingEnabled())
            .append(settingsDataSource.dateFormat())
            .append(settingsDataSource.defaultInputMethod())
            .append(settingsDataSource.developmentSettingsEnabled())
            .append(settingsDataSource.endButtonBehaviour())
        return hasher.hash(deviceStateSb.toString())
    }
}