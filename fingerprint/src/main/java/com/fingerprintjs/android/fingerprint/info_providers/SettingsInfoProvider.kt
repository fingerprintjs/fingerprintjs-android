package com.fingerprintjs.android.fingerprint.info_providers


import android.content.ContentResolver
import android.os.Build
import android.provider.Settings
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages
import com.fingerprintjs.android.fingerprint.tools.executeSafe


@Deprecated(message = DeprecationMessages.UNREACHABLE_SYMBOL_UNINTENDED_PUBLIC_API)
public interface SettingsDataSource {
    public fun adbEnabled(): String
    public fun developmentSettingsEnabled(): String
    public fun httpProxy(): String
    public fun transitionAnimationScale(): String
    public fun windowAnimationScale(): String

    // Secure
    public fun dataRoamingEnabled(): String
    public fun accessibilityEnabled(): String
    public fun defaultInputMethod(): String
    public fun rttCallingMode(): String
    public fun touchExplorationEnabled(): String

    // System
    public fun alarmAlertPath(): String
    public fun dateFormat(): String
    public fun endButtonBehaviour(): String
    public fun fontScale(): String
    public fun screenOffTimeout(): String
    public fun textAutoReplaceEnable(): String
    public fun textAutoPunctuate(): String
    public fun time12Or24(): String
}

internal class SettingsDataSourceImpl(
    private val contentResolver: ContentResolver
) : SettingsDataSource {
    //region: Global settings
    override fun adbEnabled(): String {
        return extractGlobalSettingsParam(
            Settings.Global.ADB_ENABLED
        )
    }

    override fun developmentSettingsEnabled(): String {
        return extractGlobalSettingsParam(
            Settings.Global.DEVELOPMENT_SETTINGS_ENABLED
        )
    }

    override fun httpProxy(): String {
        return extractGlobalSettingsParam(
            Settings.Global.HTTP_PROXY
        )
    }

    override fun transitionAnimationScale(): String {
        return extractGlobalSettingsParam(
            Settings.Global.TRANSITION_ANIMATION_SCALE
        )
    }

    override fun windowAnimationScale(): String {
        return extractGlobalSettingsParam(
            Settings.Global.WINDOW_ANIMATION_SCALE
        )
    }

    override fun dataRoamingEnabled(): String {
        return extractGlobalSettingsParam(
            Settings.Global.DATA_ROAMING
        )
    }
    //endregion
    //region: Secure settings


    override fun accessibilityEnabled(): String {
        return extractSecureSettingsParam(
            Settings.Secure.ACCESSIBILITY_ENABLED
        )
    }

    override fun defaultInputMethod(): String {
        return extractSecureSettingsParam(
            Settings.Secure.DEFAULT_INPUT_METHOD
        )
    }

    override fun rttCallingMode(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            extractSecureSettingsParam(
                Settings.Secure.RTT_CALLING_MODE
            )
        } else {
            ""
        }
    }

    override fun touchExplorationEnabled(): String {
        return extractSecureSettingsParam(
            Settings.Secure.TOUCH_EXPLORATION_ENABLED
        )
    }

    //endregion
    //region: System settings

    override fun alarmAlertPath(): String {
        return extractSystemSettingsParam(
            Settings.System.ALARM_ALERT
        )
    }

    override fun dateFormat(): String {
        return extractSystemSettingsParam(
            Settings.System.DATE_FORMAT
        )
    }

    override fun endButtonBehaviour(): String {
        return extractSystemSettingsParam(
            Settings.System.END_BUTTON_BEHAVIOR
        )
    }

    override fun fontScale(): String {
        return extractSystemSettingsParam(
            Settings.System.FONT_SCALE
        )
    }

    override fun screenOffTimeout(): String {
        return extractSystemSettingsParam(
            Settings.System.SCREEN_OFF_TIMEOUT
        )
    }

    override fun textAutoReplaceEnable(): String {
        return extractSystemSettingsParam(
            Settings.System.TEXT_AUTO_REPLACE
        )
    }

    override fun textAutoPunctuate(): String {
        return extractSystemSettingsParam(
            Settings.System.TEXT_AUTO_PUNCTUATE
        )
    }

    override fun time12Or24(): String {
        return extractSystemSettingsParam(
            Settings.System.TIME_12_24
        )
    }
    //endregion

    private fun extractGlobalSettingsParam(key: String): String {
        return executeSafe({
            Settings.Global.getString(contentResolver, key)
        }, "")
    }

    private fun extractSecureSettingsParam(key: String): String {
        return executeSafe({
            Settings.Secure.getString(contentResolver, key)
        }, "")
    }

    private fun extractSystemSettingsParam(key: String): String {
        return executeSafe({
            Settings.System.getString(contentResolver, key)
        }, "")
    }
}


