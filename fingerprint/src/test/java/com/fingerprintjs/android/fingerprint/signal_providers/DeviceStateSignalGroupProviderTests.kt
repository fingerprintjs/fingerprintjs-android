package com.fingerprintjs.android.fingerprint.signal_providers


import com.fingerprintjs.android.fingerprint.info_providers.DevicePersonalizationInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.DeviceSecurityInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.info_providers.FingerprintSensorStatus
import com.fingerprintjs.android.fingerprint.info_providers.SettingsDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalGroupProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test


class DeviceStateSignalGroupProviderTests {
    @Test
    fun `DeviceStateFingerprinter v1 - success`() {
        val deviceSecurityInfoProvider = createDeviceStateSignalGroupProvider(1)
        assertEquals(
            "adbEnableddevelopmentSettingsEnabledhttpProxytransitionAnimationScalewindowAnimationScaledataRoamingEnabledaccessibilityEnableddefaultInputMethodrttCallingModetouchExplorationEnabledalarmAlertPathdateFormatendButtonBehaviourfontScalescreenOffTimeouttextAutoReplaceEnabletextAutoPunctuatetime12Or24truesupportedHotelCaliforniaEN-USRU-ru",
            deviceSecurityInfoProvider.fingerprint(StabilityLevel.UNIQUE)
        )
    }

    @Test
    fun `DeviceStateFingerprinter v2 - success`() {
        val deviceSecurityInfoProvider = createDeviceStateSignalGroupProvider(2)
        assertEquals(
            "adbEnableddevelopmentSettingsEnabledhttpProxytransitionAnimationScalewindowAnimationScaledataRoamingEnabledaccessibilityEnableddefaultInputMethodtouchExplorationEnabledalarmAlertPathdateFormatendButtonBehaviourfontScalescreenOffTimeouttime12Or24truesupportedHotelCaliforniaEN-USRU-ruUSAGreenwichEN-US",
            deviceSecurityInfoProvider.fingerprint(stabilityLevel = StabilityLevel.UNIQUE)
        )
    }

    private fun createDeviceStateSignalGroupProvider(version: Int): DeviceStateSignalGroupProvider {
        val settingsDataSource = object :
            SettingsDataSource {
            override fun adbEnabled() = "adbEnabled"
            override fun developmentSettingsEnabled() = "developmentSettingsEnabled"
            override fun httpProxy() = "httpProxy"
            override fun transitionAnimationScale() = "transitionAnimationScale"
            override fun windowAnimationScale() = "windowAnimationScale"
            override fun dataRoamingEnabled() = "dataRoamingEnabled"
            override fun accessibilityEnabled() = "accessibilityEnabled"
            override fun defaultInputMethod() = "defaultInputMethod"
            override fun rttCallingMode() = "rttCallingMode"
            override fun touchExplorationEnabled() = "touchExplorationEnabled"
            override fun alarmAlertPath() = "alarmAlertPath"
            override fun dateFormat() = "dateFormat"
            override fun endButtonBehaviour() = "endButtonBehaviour"
            override fun fontScale() = "fontScale"
            override fun screenOffTimeout() = "screenOffTimeout"
            override fun textAutoReplaceEnable() = "textAutoReplaceEnable"
            override fun textAutoPunctuate() = "textAutoPunctuate"
            override fun time12Or24() = "time12Or24"
        }

        val devicePersonalizationDataSource = object :
            DevicePersonalizationInfoProvider {
            override fun ringtoneSource() = "HotelCalifornia"
            override fun availableLocales() = arrayOf("EN-US", "RU-ru")
            override fun regionCountry() = "USA"

            override fun defaultLanguage() = "EN-US"

            override fun timezone() = "Greenwich"
        }

        val deviceSecurityInfoProvider = object :
            DeviceSecurityInfoProvider {
            override fun encryptionStatus() = "inactive"

            override fun securityProvidersData() =
                listOf(Pair("Bouncy castle", "1.2.1"), Pair("Sun security", "2.0.0"))

            override fun isPinSecurityEnabled() = true
        }

        val fingerprintSensorInfoProvider = object :
            FingerprintSensorInfoProvider {
            override fun getStatus() = FingerprintSensorStatus.SUPPORTED
        }

        return DeviceStateSignalGroupProvider(
            settingsDataSource,
            devicePersonalizationDataSource,
            deviceSecurityInfoProvider,
            fingerprintSensorInfoProvider,
            EmptyHasher(),
            version
        )
    }
}