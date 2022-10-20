package com.fingerprintjs.android.fingerprint.signal_providers.device_state


import com.fingerprintjs.android.fingerprint.info_providers.DevicePersonalizationInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.DeviceSecurityInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.SettingsDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.SignalGroupProvider
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.hashers.Hasher


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public class DeviceStateSignalGroupProvider(
    settingsDataSource: SettingsDataSource,
    devicePersonalizationInfoProvider: DevicePersonalizationInfoProvider,
    deviceSecurityInfoProvider: DeviceSecurityInfoProvider,
    fingerprintSensorInfoProvider: FingerprintSensorInfoProvider,
    private val hasher: Hasher,
    version: Int
) : SignalGroupProvider<DeviceStateRawData>(version) {

    private val rawData by lazy {
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
            devicePersonalizationInfoProvider.ringtoneSource(),
            devicePersonalizationInfoProvider.availableLocales().toList(),
            devicePersonalizationInfoProvider.regionCountry(),
            devicePersonalizationInfoProvider.defaultLanguage(),
            devicePersonalizationInfoProvider.timezone()
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

    override fun rawData(): DeviceStateRawData = rawData

    private fun v1() = listOf(
        rawData.adbEnabled(),
        rawData.developmentSettingsEnabled(),
        rawData.httpProxy(),
        rawData.transitionAnimationScale(),
        rawData.windowAnimationScale(),
        rawData.dataRoamingEnabled(),
        rawData.accessibilityEnabled(),
        rawData.defaultInputMethod(),
        rawData.rttCallingMode(),
        rawData.touchExplorationEnabled(),
        rawData.alarmAlertPath(),
        rawData.dateFormat(),
        rawData.endButtonBehaviour(),
        rawData.fontScale(),
        rawData.screenOffTimeout(),
        rawData.textAutoReplaceEnable(),
        rawData.textAutoPunctuate(),
        rawData.time12Or24(),
        rawData.isPinSecurityEnabled(),
        rawData.fingerprintSensorStatus(),
        rawData.ringtoneSource(),
        rawData.availableLocales()
    )

    private fun v2() = listOf(
        rawData.adbEnabled(),
        rawData.developmentSettingsEnabled(),
        rawData.httpProxy(),
        rawData.transitionAnimationScale(),
        rawData.windowAnimationScale(),
        rawData.dataRoamingEnabled(),
        rawData.accessibilityEnabled(),
        rawData.defaultInputMethod(),
        rawData.touchExplorationEnabled(),
        rawData.alarmAlertPath(),
        rawData.dateFormat(),
        rawData.endButtonBehaviour(),
        rawData.fontScale(),
        rawData.screenOffTimeout(),
        rawData.time12Or24(),
        rawData.isPinSecurityEnabled(),
        rawData.fingerprintSensorStatus(),
        rawData.ringtoneSource(),
        rawData.availableLocales(),
        rawData.regionCountry(),
        rawData.timezone(),
        rawData.defaultLanguage(),
    )
}