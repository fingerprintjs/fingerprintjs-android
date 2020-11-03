package com.fingerprintjs.android.fingerprint.fingerprinters.device_state


data class DeviceStateRawData(
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

    val accelerometerRotationEnabled: String,
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
    val availableLocales: List<String>
)