package com.fingerprintjs.android.fingerprint.signal_providers.device_state


import com.fingerprintjs.android.fingerprint.info_providers.DevicePersonalizationDataSource
import com.fingerprintjs.android.fingerprint.info_providers.DeviceSecurityInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.SettingsDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


class DeviceStateSignalGroupProvider(
    settingsDataSource: SettingsDataSource,
    devicePersonalizationDataSource: DevicePersonalizationDataSource,
    deviceSecurityInfoProvider: DeviceSecurityInfoProvider,
    fingerprintSensorInfoProvider: FingerprintSensorInfoProvider,
    private val hasher: Hasher,
    version: Int
) : SignalGroupProvider<DeviceStateRawData>(version) {

    private val rawData =
        DeviceStateRawData(
            settingsDataSource.adbEnabled(),
            settingsDataSource.developmentSettingsEnabled(),
            settingsDataSource.httpProxy(),
            settingsDataSource.transitionAnimationScale(),
            settingsDataSource.windowAnimationScale(),
            settingsDataSource.dataRoamingEnabled(),
            settingsDataSource.accessibilityEnabled(),
            settingsDataSource.defaultInputMethod(),
            settingsDataSource.rttCallingMode(),
            settingsDataSource.touchExplorationEnabled(),
            settingsDataSource.alarmAlertPath(),
            settingsDataSource.dateFormat(),
            settingsDataSource.endButtonBehaviour(),
            settingsDataSource.fontScale(),
            settingsDataSource.screenOffTimeout(),
            settingsDataSource.textAutoReplaceEnable(),
            settingsDataSource.textAutoPunctuate(),
            settingsDataSource.time12Or24(),
            deviceSecurityInfoProvider.isPinSecurityEnabled(),
            fingerprintSensorInfoProvider.getStatus().stringDescription,
            devicePersonalizationDataSource.ringtoneSource(),
            devicePersonalizationDataSource.availableLocales().toList(),
            devicePersonalizationDataSource.regionCountry(),
            devicePersonalizationDataSource.defaultLanguage(),
            devicePersonalizationDataSource.timezone()
        )

    override fun fingerprint(): String {
        return hasher.hash(when (version) {
            1 -> v1()
            2 -> v2()
            else -> v2()
        })
    }

    override fun rawData() = rawData

    private fun v1(): String {
        val deviceStateSb = StringBuilder()
        deviceStateSb
            .append(rawData.adbEnabled)
            .append(rawData.developmentSettingsEnabled)
            .append(rawData.httpProxy)
            .append(rawData.transitionAnimationScale)
            .append(rawData.windowAnimationScale)

            .append(rawData.dataRoamingEnabled)
            .append(rawData.accessibilityEnabled)
            .append(rawData.defaultInputMethod)
            .append(rawData.rttCallingMode)
            .append(rawData.touchExplorationEnabled)

            .append(rawData.alarmAlertPath)
            .append(rawData.dateFormat)
            .append(rawData.endButtonBehaviour)
            .append(rawData.fontScale)
            .append(rawData.screenOffTimeout)
            .append(rawData.textAutoReplaceEnable)
            .append(rawData.textAutoPunctuate)
            .append(rawData.time12Or24)
            .append(rawData.isPinSecurityEnabled)
            .append(rawData.fingerprintSensorStatus)
            .append(rawData.ringtoneSource)

        rawData.availableLocales.forEach {
            deviceStateSb.append(it)
        }

        return deviceStateSb.toString()
    }

    private fun v2(): String {
        val deviceStateSb = StringBuilder()
        deviceStateSb
            .append(rawData.adbEnabled)
            .append(rawData.developmentSettingsEnabled)
            .append(rawData.httpProxy)
            .append(rawData.transitionAnimationScale)
            .append(rawData.windowAnimationScale)

            .append(rawData.dataRoamingEnabled)
            .append(rawData.accessibilityEnabled)
            .append(rawData.defaultInputMethod)
            .append(rawData.touchExplorationEnabled)

            .append(rawData.alarmAlertPath)
            .append(rawData.dateFormat)
            .append(rawData.endButtonBehaviour)
            .append(rawData.fontScale)
            .append(rawData.screenOffTimeout)
            .append(rawData.time12Or24)
            .append(rawData.isPinSecurityEnabled)
            .append(rawData.fingerprintSensorStatus)
            .append(rawData.ringtoneSource)
            .append(rawData.regionCountry)
            .append(rawData.timezone)
            .append(rawData.defaultLanguage)

        rawData.availableLocales.forEach {
            deviceStateSb.append(it)
        }

        return deviceStateSb.toString()
    }
}