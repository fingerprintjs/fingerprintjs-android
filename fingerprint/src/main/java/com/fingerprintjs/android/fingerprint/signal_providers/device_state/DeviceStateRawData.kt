package com.fingerprintjs.android.fingerprint.signal_providers.device_state


import com.fingerprintjs.android.fingerprint.signal_providers.IdentificationSignal
import com.fingerprintjs.android.fingerprint.signal_providers.RawData
import com.fingerprintjs.android.fingerprint.signal_providers.StabilityLevel
import com.fingerprintjs.android.fingerprint.tools.DeprecationMessages


@Deprecated(message = DeprecationMessages.DEPRECATED_SYMBOL)
public data class DeviceStateRawData(
    val adbEnabled: String,
    val developmentSettingsEnabled: String,
    val httpProxy: String,
    val transitionAnimationScale: String,
    val windowAnimationScale: String,

    val dataRoamingEnabled: String,
    val accessibilityEnabled: String,
    val defaultInputMethod: String,
    val rttCallingMode: String,
    val touchExplorationEnabled: String,

    val alarmAlertPath: String,
    val dateFormat: String,
    val endButtonBehaviour: String,
    val fontScale: String,
    val screenOffTimeout: String,
    val textAutoReplaceEnable: String,
    val textAutoPunctuate: String,
    val time12Or24: String,

    val isPinSecurityEnabled: Boolean,
    val fingerprintSensorStatus: String,

    val ringtoneSource: String,
    val availableLocales: List<String>,

    val regionCountry: String,
    val defaultLanguage: String,
    val timezone: String
) : RawData() {

    override fun signals(): List<IdentificationSignal<out Any>> = listOf(
        adbEnabled(),
        developmentSettingsEnabled(),
        httpProxy(),
        transitionAnimationScale(),
        windowAnimationScale(),
        dataRoamingEnabled(),
        accessibilityEnabled(),
        defaultInputMethod(),
        rttCallingMode(),
        touchExplorationEnabled(),
        alarmAlertPath(),
        dateFormat(),
        endButtonBehaviour(),
        fontScale(),
        screenOffTimeout(),
        textAutoReplaceEnable(),
        textAutoPunctuate(),
        time12Or24(),
        isPinSecurityEnabled(),
        fingerprintSensorStatus(),
        ringtoneSource(),
        availableLocales(),
        regionCountry(),
        defaultLanguage(),
        timezone()
    )

    public fun adbEnabled(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        ADB_ENABLED_KEY,
        ADB_ENABLED_DISPLAY_NAME,
        adbEnabled
    ) {
        override fun toString() = adbEnabled
    }

    public fun developmentSettingsEnabled(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        DEVELOPMENT_SETTINGS_ENABLED_KEY,
        DEVELOPMENT_SETTINGS_DISPLAY_NAME,
        developmentSettingsEnabled
    ) {
        override fun toString() = developmentSettingsEnabled
    }

    public fun httpProxy(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.UNIQUE,
        HTTP_PROXY_KEY,
        HTTP_PROXY_DISPLAY_NAME,
        httpProxy
    ) {
        override fun toString() = httpProxy
    }

    public fun transitionAnimationScale(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        TRANSITION_ANIMATION_SCALE_KEY,
        TRANSITION_ANIMATION_SCALE_DISPLAY_NAME,
        transitionAnimationScale
    ) {
        override fun toString() = transitionAnimationScale
    }

    public fun windowAnimationScale(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        WINDOW_ANIMATION_SCALE_KEY,
        WINDOW_ANIMATION_SCALE_DISPLAY_NAME,
        windowAnimationScale
    ) {
        override fun toString() = windowAnimationScale
    }

    public fun dataRoamingEnabled(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.UNIQUE,
        DATA_ROAMING_ENABLED_KEY,
        DATA_ROAMING_ENABLED_DISPLAY_NAME,
        dataRoamingEnabled
    ) {
        override fun toString() = dataRoamingEnabled
    }

    public fun accessibilityEnabled(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        ACCESSIBILITY_ENABLED_KEY,
        ACCESSIBILITY_ENABLED_DISPLAY_NAME,
        accessibilityEnabled
    ) {
        override fun toString() = accessibilityEnabled
    }

    public fun defaultInputMethod(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        DEFAULT_INPUT_METHOD_KEY,
        DEFAULT_INPUT_METHOD_DISPLAY_NAME,
        defaultInputMethod

    ) {
        override fun toString() = defaultInputMethod
    }

    public fun rttCallingMode(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        2,
        StabilityLevel.OPTIMAL,
        RTT_CALLING_MODE_KEY,
        RTT_CALLING_MODE_DISPLAY_NAME,
        rttCallingMode
    ) {
        override fun toString() = rttCallingMode
    }

    public fun touchExplorationEnabled(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        TOUCH_EXPLORATION_ENABLED_KEY,
        TOUCH_EXPLORATION_ENABLDE_DISPLAY_NAME,
        touchExplorationEnabled
    ) {
        override fun toString() = touchExplorationEnabled
    }

    public fun alarmAlertPath(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        ALARM_ALERT_PATH_KEY,
        ALARM_ALERT_PATH_DISPLAY_NAME,
        alarmAlertPath
    ) {
        override fun toString() = alarmAlertPath
    }

    public fun dateFormat(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        DATE_FORMAT_KEY,
        DATE_FORMAT_DISPLAY_NAME,
        dateFormat
    ) {
        override fun toString() = dateFormat
    }

    public fun endButtonBehaviour(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        END_BUTTON_BEHAVIOUR_KEY,
        END_BUTTON_BEHAVIOUR_DISPLAY_NAME,
        endButtonBehaviour
    ) {
        override fun toString() = endButtonBehaviour
    }

    public fun fontScale(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        FONT_SCALE_KEY,
        FONT_SCALE_DISPLAY_NAME,
        fontScale
    ) {
        override fun toString() = fontScale
    }

    public fun screenOffTimeout(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        SCREEN_OFF_TIMEOUT_KEY,
        SCREEN_OFF_TIMEOUT_DISPLAY_NAME,
        screenOffTimeout
    ) {
        override fun toString() = screenOffTimeout
    }

    public fun textAutoReplaceEnable(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        2,
        StabilityLevel.OPTIMAL,
        TEXT_AUTO_REPLACE_ENABLE_KEY,
        TEXT_AUTO_REPLACE_ENABLE_DISPLAY_NAME,
        textAutoReplaceEnable
    ) {
        override fun toString() = textAutoReplaceEnable
    }

    public fun textAutoPunctuate(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        2,
        StabilityLevel.OPTIMAL,
        TEXT_AUTO_PUNCTUATE_KEY,
        TEXT_AUTO_PUNCTUATE_DISPLAY_NAME,
        textAutoPunctuate
    ) {
        override fun toString() = textAutoPunctuate
    }

    public fun time12Or24(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        TIME_12_OR_24_KEY,
        TIME_12_OR_24_DISPLAY_NAME,
        time12Or24
    ) {
        override fun toString() = time12Or24
    }

    public fun isPinSecurityEnabled(): IdentificationSignal<Boolean> = object : IdentificationSignal<Boolean>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        IS_PIN_SECURITY_ENABLED_KEY,
        IS_PIN_SECURITY_ENABLED_DISPLAY_NAME,
        isPinSecurityEnabled
    ) {
        override fun toString() = isPinSecurityEnabled.toString()
    }

    public fun fingerprintSensorStatus(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        FINGERPRINT_SENSOR_STATUS_KEY,
        FINGERPRINT_SENSOR_STATUS_DISPLAY_NAME,
        fingerprintSensorStatus
    ) {
        override fun toString() = fingerprintSensorStatus
    }

    public fun ringtoneSource(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        RINGTONE_SOURCE_KEY,
        RINGTONE_SOURCE_DISPLAY_NAME,
        ringtoneSource
    ) {
        override fun toString() = ringtoneSource
    }

    public fun availableLocales(): IdentificationSignal<List<String>> = object : IdentificationSignal<List<String>>(
        1,
        null,
        StabilityLevel.OPTIMAL,
        AVAILABLE_LOCALES_KEY,
        AVAILABLE_LOCALES_DISPLAY_NAME,
        availableLocales
    ) {
        override fun toString(): String {
            val sb = StringBuilder()
            availableLocales.forEach {
                sb.append(it)
            }
            return sb.toString()
        }
    }

    public fun regionCountry(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        REGION_COUNTRY_KEY,
        REGION_COUNTRY_DISPLAY_NAME,
        regionCountry
    ) {
        override fun toString() = regionCountry
    }

    public fun defaultLanguage(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        DEFAULT_LANGUAGE_KEY,
        DEFAULT_LANGUAGE_DISPLAY_NAME,
        defaultLanguage
    ) {
        override fun toString() = defaultLanguage
    }

    public fun timezone(): IdentificationSignal<String> = object : IdentificationSignal<String>(
        2,
        null,
        StabilityLevel.OPTIMAL,
        TIMEZONE_KEY,
        TIMEZONE_DISPLAY_NAME,
        timezone
    ) {
        override fun toString() = timezone
    }
}