package com.fingerprintjs.android.fingerprint.signal_providers

import com.fingerprintjs.android.fingerprint.datasources.DevicePersonalizationDataSource
import com.fingerprintjs.android.fingerprint.datasources.FingerprintSensorInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.FingerprintSensorStatus
import com.fingerprintjs.android.fingerprint.datasources.KeyGuardInfoProvider
import com.fingerprintjs.android.fingerprint.datasources.SettingsDataSource
import com.fingerprintjs.android.fingerprint.signal_providers.device_state.DeviceStateSignalProvider
import com.fingerprintjs.android.fingerprint.tools.hashers.EmptyHasher
import junit.framework.Assert.assertEquals
import org.junit.Test

class DeviceStateSignalProviderTests {
    @Test
    fun `DeviceStateFingerprinter v1 - success`() {
        val settingsDataSource = object : SettingsDataSource {
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

        val devicePersonalizationDataSource = object : DevicePersonalizationDataSource {
            override fun ringtoneSource() = "HotelCalifornia"
            override fun availableLocales() = arrayOf("EN-US", "RU-ru")
        }

        val keyGuardInfoProvider = object : KeyGuardInfoProvider {
            override fun isPinSecurityEnabled() = true
        }

        val fingerprintSensorInfoProvider = object : FingerprintSensorInfoProvider {
            override fun getStatus() = FingerprintSensorStatus.SUPPORTED
        }

        val fingerprinter =
            DeviceStateSignalProvider(
                settingsDataSource,
                devicePersonalizationDataSource,
                keyGuardInfoProvider,
                fingerprintSensorInfoProvider,
                EmptyHasher(),
                1
            )

        assertEquals(
            "adbEnableddevelopmentSettingsEnabledhttpProxytransition" +
                    "AnimationScalewindowAnimationScaledataRoamingEnabledaccessibilityEnabled" +
                    "defaultInputMethodrttCallingModetouchExplorationEnabledalarmAlertPath" +
                    "dateFormatendButtonBehaviourfontScalescreenOffTimeouttextAutoReplaceEnable" +
                    "textAutoPunctuatetime12Or24truesupportedHotelCaliforniaEN-USRU-ru"
            , fingerprinter.fingerprint()
        )
    }
}